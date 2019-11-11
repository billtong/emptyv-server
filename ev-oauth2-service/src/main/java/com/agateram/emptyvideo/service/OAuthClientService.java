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
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Service
@Slf4j
public class OAuthClientService {
    @Autowired
    UserWebClient userWebClient;
    @Autowired
    private OAuthClientRepository oAuthClientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Mono<ServerResponse> getUserInfo(ServerRequest serverRequest) {
        return serverRequest.principal().flatMap(principal -> {
            String userId = principal.getName();
            return userWebClient.getUserById(userId)
                    .flatMap(clientResponse -> {
                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            return ok().body(clientResponse.bodyToMono(Map.class), Map.class);
                        }
                        return status(401).build();
                    });
        });
    }

    public Mono<ServerResponse> createNewClient(ServerRequest serverRequest) {
        String redirectURL = serverRequest.queryParam("redirectURL").get();
        OAuthClient oAuthClient = new OAuthClient();
        oAuthClient.setRedirectUri(redirectURL);
        byte[] array = new byte[20];
        new Random().nextBytes(array);
        String secret = new String(array, Charset.forName("UTF-8"));
        oAuthClient.setSecret(secret);
        return oAuthClientRepository.insert(oAuthClient)
                .flatMap(oAuthClient1 -> status(201).body(Mono.just(oAuthClient1), OAuthClient.class));
    }

    public Mono<ServerResponse> generateOauthCode(ServerRequest serverRequest) {
        String clientId = serverRequest.queryParam("clientId").get();
        String secret = serverRequest.queryParam("secret").get();
        Mono<MultiValueMap<String, String>> formDataMono = serverRequest.formData();
        Mono<OAuthClient> oAuthClientMono = oAuthClientRepository.findById(clientId);

        return Mono.zip(formDataMono, oAuthClientMono, Mono.just(secret))
                .flatMap(tuple -> {
                    MultiValueMap<String, String> formData = tuple.getT1();
                    final OAuthClient oAuthClient = tuple.getT2();
                    if (oAuthClient != null && oAuthClient.getSecret().equals(tuple.getT3())) {
                        String email = formData.getFirst("userEmail");
                        String pwd = formData.getFirst("userPassword");
                        return userWebClient.getAuthTokenByLogin(email, pwd).flatMap(clientResponse -> {
                            log.info(clientResponse.statusCode().toString());
                            log.info(clientResponse.toString());
                            if (clientResponse.statusCode().is2xxSuccessful()) {
                                return clientResponse.bodyToMono(Map.class).flatMap(map -> {
                                    String userId = String.valueOf(map.get("id"));
                                    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                                    for (String scope : oAuthClient.getScope()) {
                                        grantedAuthorities.add(new SimpleGrantedAuthority(scope));
                                    }
                                    String code = JWTTokenService.generateToken(userId, oAuthClient.getSecret(), grantedAuthorities);
                                    return ServerResponse.temporaryRedirect(URI.create(oAuthClient.getRedirectUri()))
                                            .header(HttpHeaders.AUTHORIZATION, String.join(" ", "Bearer", code))
                                            .build();
                                });
                            } else {
                                String path = MessageFormat.format("/?clientId={0}&secret={1}&error={2}", oAuthClient.getId(), oAuthClient.getSecret(), "failed");
                                return ServerResponse.temporaryRedirect(URI.create(path)).build();
                            }
                        });
                    }
                    return status(401).build();
                });
    }
}
