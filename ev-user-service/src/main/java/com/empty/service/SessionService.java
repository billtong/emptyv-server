package com.empty.service;

import com.empty.domain.Session;
import com.empty.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
public class SessionService {
    @Autowired
    SessionRepository sessionRepository;

    public Mono<ServerResponse> getSessionById(ServerRequest serverRequest) {
        return ok().body(sessionRepository.findById(serverRequest.pathVariable("sessionId")), Session.class);
    }
}
