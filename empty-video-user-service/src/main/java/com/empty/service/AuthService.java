package com.empty.service;

import com.empty.domain.Session;
import com.empty.domain.User;
import com.empty.repository.SessionRepository;
import com.empty.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Map;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public Mono<ServerResponse> getMapAuthMono(ServerRequest serverRequest) {
        return serverRequest.principal().flatMap(principal -> userRepository.findById(principal.getName())
                .flatMap(User::getPublicUserMapMono)).flatMap(map -> ok().body(Mono.just(map), Map.class));
    }

    public Mono<ServerResponse> activeAccount(ServerRequest serverRequest) {
        String sessionId = serverRequest.pathVariable("sessionId");
        return sessionRepository.findById(sessionId).flatMap(session -> {
            Mono<User> userMono = userRepository.findById(session.getUserId());
            Mono<Session> sessionMono = Mono.justOrEmpty(session);
            return Mono.zip(userMono, sessionMono).flatMap(tuple1 -> {
                User user = tuple1.getT1();
                Session session1 = tuple1.getT2();
                user.getSystem().setActive(true);
                Mono<User> activeUser = userRepository.save(user);
                Mono<ServerResponse> serverResponseMono = ok().build();
                Mono<Void> voidMono = sessionRepository.deleteById(session1.getId());
                return Mono.zip(activeUser, serverResponseMono, voidMono).map(Tuple2::getT2);
            });
        });
    }
}
