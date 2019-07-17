package com.empty.service;

import com.empty.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    public Mono<ServerResponse> getByUserId(ServerRequest serverRequest) {
        String userId = String.valueOf(serverRequest.attributes().get("userId"));
        return notificationRepository.findAllByToOrderByCreatedDesc(userId).collectList().flatMap(
                notifications -> ok().body(Mono.just(notifications), List.class));
    }
}
