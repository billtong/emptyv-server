package com.agateram.emptyvideo.service;

import com.agateram.emptyvideo.auth.jwt.JWTTokenService;
import com.agateram.emptyvideo.domain.OAuthClient;
import com.agateram.emptyvideo.repository.OAuthClientRepository;
import com.agateram.emptyvideo.web.UserWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.net.URI;
import java.text.MessageFormat;
import java.util.*;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Service
@Slf4j
public class OAuthClientService {
    @Autowired
    private OAuthClientRepository oAuthClientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserWebClient userWebClient;

    public Mono<ServerResponse> getUserInfo(ServerRequest serverRequest) {
        return serverRequest.principal().flatMap(principal -> {
            String clientId = principal.getName();
            return oAuthClientRepository.findById(clientId)
                    .flatMap(oAuthClient -> userWebClient.getUserByAuthToken(oAuthClient.getUserBearerToken())
                            .flatMap(clientResponse -> {
                                if (clientResponse.statusCode().is2xxSuccessful()) {
                                    return ok().body(clientResponse.bodyToMono(Map.class), Map.class);
                                }
                                return status(401).build();
                            })
                );
        });
    }

    public Mono<ServerResponse> createNewClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(OAuthClient.class)
        .flatMap(oAuthClient -> oAuthClientRepository.insert(oAuthClient).then(status(201).build()));
    }

    public Mono<ServerResponse> getOauthCode(ServerRequest serverRequest) {
        String clientId = serverRequest.queryParam("clientId").get();
        String secret = serverRequest.queryParam("secret").get();
        Mono<MultiValueMap<String, String>> formDataMono = serverRequest.formData();
        Mono<OAuthClient> oAuthClientMono = oAuthClientRepository.findById(clientId);

        return Mono.zip(formDataMono, oAuthClientMono, Mono.just(secret))
                .flatMap(tuple -> {
            MultiValueMap<String, String> formData = tuple.getT1();
            OAuthClient oAuthClient = tuple.getT2();
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (String scope : oAuthClient.getScope()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(scope));
            }
            if (oAuthClient != null && oAuthClient.getSecret().equals(tuple.getT3())) {
                final String code = JWTTokenService.generateToken(oAuthClient.getId(),oAuthClient.getSecret(), grantedAuthorities);
                String email = formData.getFirst("userEmail");
                String pwd = formData.getFirst("userPassword");
                log.info(formData.toString());
                return userWebClient.getAuthTokenByLogin(email, pwd).flatMap(clientResponse -> {
                    log.info(clientResponse.statusCode().toString());
                    log.info(clientResponse.toString());
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        String bearerToken = clientResponse.headers().header(HttpHeaders.AUTHORIZATION).get(0);
                        oAuthClient.setUserBearerToken(bearerToken);
                        Mono<OAuthClient> oAuthClientMono1 = oAuthClientRepository.save(oAuthClient);
                        Mono<ServerResponse> serverResponseMono = ServerResponse.temporaryRedirect(URI.create(oAuthClient.getRedirectUri()))
                                .header(HttpHeaders.AUTHORIZATION, String.join(" ", "Bearer", code))
                                .build();
                        return Mono.zip(oAuthClientMono1, serverResponseMono).map(Tuple2::getT2);
                    } else {
                        String path = MessageFormat.format("/?clientId={0}&secret={1}&error={2}",oAuthClient.getId(), oAuthClient.getSecret(), "failed");
                        return ServerResponse.temporaryRedirect(URI.create(path)).build();
                    }
                });
            }
            return status(401).build();
        });
    }
}
