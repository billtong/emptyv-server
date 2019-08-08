package com.empty.service;

import com.empty.domain.Session;
import com.empty.domain.User;
import com.empty.repository.SessionRepository;
import com.empty.repository.UserRepository;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Service
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final EmailService emailService;
    private final EurekaClient eurekaClient;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, SessionRepository sessionRepository, EmailService emailService, EurekaClient eurekaClient) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.emailService = emailService;
        this.eurekaClient = eurekaClient;
    }

    public Mono<ServerResponse> getUserById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return userRepository.findById(id).flatMap(
                user -> ok().body(user.getPublicUserMapMono(), Map.class)
        );
    }

    public Mono<ServerResponse> getUsersByIdList(ServerRequest serverRequest) {
        String ids = serverRequest.queryParam("ids").get();
        List<String> idList = Arrays.asList(ids.split(","));
        return userRepository.findAllById(idList).collectList().flatMap(
                users -> ok().body(Mono.just(users), List.class));
    }

    public Mono<ServerResponse> register(ServerRequest serverRequest) {
        Mono<Map> userInfoMono = serverRequest.bodyToMono(Map.class);
        Mono<WebSession> sessionMono = serverRequest.session();
        return Mono.zip(userInfoMono, sessionMono).flatMap(tuple1 -> {
            User user = new User(tuple1.getT1(), passwordEncoder);
            Mono<User> newUser = userRepository.insert(user);
            Mono<WebSession> sessionMono1 = Mono.just(tuple1.getT2());
            return Mono.zip(newUser, sessionMono1).flatMap(tuple2 -> {
                User savedUser = tuple2.getT1();
                WebSession webSession = tuple2.getT2();
                Mono<Session> savedSession = sessionRepository.insert(new Session(webSession, savedUser.getId()));
                Mono<ServerResponse> responseMono = status(201).build();

                InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("api-gateway", false);
                String baseUrl = instanceInfo.getHomePageUrl();
                String activeUrl = MessageFormat.format("{0}user-service/auth/active/{1}", baseUrl, webSession.getId());
                //log.info(activeUrl);
                String to = savedUser.getEmail();
                String username = savedUser.getProfile().getName();
                /*
                emailService.sendActivateEmail(to, activeUrl, username);
                */
                return Mono.zip(savedSession, responseMono).map(Tuple2::getT2);
            });
        });
    }

    public Mono<ServerResponse> updateProfile(ServerRequest serverRequest) {
        Mono<User> userMono = serverRequest.principal().zipWith(Mono.just(serverRequest))
                .flatMap(tuple -> {
                    tuple.getT2().attributes().put("userId", tuple.getT1().getName());
                    return userRepository.findById(tuple.getT1().getName());
                });
        Mono<Map> mapMono = serverRequest.bodyToMono(Map.class);
        return Mono.zip(userMono, mapMono).flatMap(t -> {
            User finalUser = t.getT1();
            Map updateMap = t.getT2();
            finalUser.updateUserProfile((Map) updateMap.get("profile"));
            Map<String, String> msg = new HashMap<>();
            Mono<User> userMono1 = userRepository.save(finalUser);
            Mono<ServerResponse> serverResponseMono = ok().body(Mono.just(msg), Map.class);
            msg.put("message", "success");
            return Mono.zip(userMono1, serverResponseMono).map(Tuple2::getT2);
        });
    }
}
