package com.empty.service;

import com.empty.domain.Dan;
import com.empty.repository.DanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Service
public class DanService {
    @Autowired
    DanRepository danRepository;

    public Mono<ServerResponse> getDanFluxByVideoId(ServerRequest serverRequest) {
        String videoId = serverRequest.pathVariable("videoId");
        return danRepository.findAllByVideoIdOrderByVideoTimeAsc(videoId).collectList()
                .flatMap(dans -> ok().body(Mono.just(dans), List.class));
    }

    public Mono<ServerResponse> write(ServerRequest serverRequest) {
        String authId = String.valueOf(serverRequest.attribute("authId").get());
        Mono<Dan> danMono = serverRequest.bodyToMono(Dan.class);
        return Mono.zip(Mono.just(authId), danMono).flatMap(tuple -> {
            String userId = tuple.getT1();
            Dan dan = tuple.getT2();
            dan.setUserId(userId);
            return danRepository.save(dan).then(status(201).build());
        });
    }
}
