package com.empty.service;

import com.empty.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
public class HistoryService {
    @Autowired
    HistoryRepository historyRepository;

    public Mono<ServerResponse> getUserHistory(ServerRequest serverRequest) {
        String userId = String.valueOf(serverRequest.attributes().get("userId"));
        return historyRepository.findAllByUserIdOrderByCreatedDesc(userId).collectList()
                .flatMap(histories -> ok().body(Mono.just(histories), List.class));
    }
}
