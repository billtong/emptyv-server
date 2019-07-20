package com.empty.service;

import com.empty.domain.Tag;
import com.empty.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    public Mono<ServerResponse> getTagByName(ServerRequest serverRequest) {
        String name = serverRequest.pathVariable("name");
        return tagRepository.findByName(name).flatMap(tag -> ok().body(Mono.just(tag), Tag.class));
    }

    public Mono<ServerResponse> getAllTags(ServerRequest serverRequest) {
        return tagRepository.findAll().collectList().flatMap(tags -> ok().body(Mono.just(tags), List.class));
    }
}
