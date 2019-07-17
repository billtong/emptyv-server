package com.emptyvideo.ums.service;

import com.emptyvideo.ums.domain.Session;
import com.emptyvideo.ums.domain.User;
import com.emptyvideo.ums.repository.SessionRepository;
import com.emptyvideo.ums.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Service
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public UserService(UserRepository userRepository, SessionRepository sessionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<ServerResponse> getUser(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return userRepository.findById(id).flatMap(
                user -> ok().body(user.getPublicUserMapMono(), Map.class)
        ).switchIfEmpty(notFound().build());
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
                String activeUrl = "http://localhost:8001/auth/active/".concat(webSession.getId());
                log.info(activeUrl);
                //send this info to kafka message breaker start
                //code
                //send this info to kafka message breaker end
                return Mono.zip(savedSession, responseMono).map(Tuple2::getT2).switchIfEmpty(status(400).build());
            });
        });
    }

    public Mono<ServerResponse> updateProfile(ServerRequest serverRequest) {
        Mono<User> userMono = serverRequest.principal().flatMap(user -> userRepository.findById(user.getName()));
        Mono<Map> mapMono = serverRequest.bodyToMono(Map.class);
        return Mono.zip(userMono, mapMono).flatMap(t -> {
            User finalUser = t.getT1();
            finalUser.updateUserProfile(t.getT2());
            Map<String, String> msg = new HashMap<>();
            Mono<User> userMono1 = userRepository.save(finalUser);
            Mono<ServerResponse> serverResponseMono = ok().body(Mono.just(msg), Map.class);
            msg.put("message", "success");
            return Mono.zip(userMono1, serverResponseMono).map(Tuple2::getT2);
        }).switchIfEmpty(status(400).build());
    }
}
