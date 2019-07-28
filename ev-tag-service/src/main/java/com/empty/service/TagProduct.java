package com.empty.service;

import com.empty.domain.Tag;
import com.empty.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class TagProduct {
    @Autowired
    TagRepository tagRepository;

    public Flux<Tag> handleAllTags(String videoId, List<String> tags) {
        return Flux.fromIterable(tags).flatMap(tag -> handleOneTag(videoId, tag));
    }

    public Mono<Tag> handleOneTag(String videoId, String tag) {
        return tagRepository.findByName(tag)
                .switchIfEmpty(Mono.just(new Tag(tag)))
                .doOnSuccess(tag1 -> {
                    List<String> oldVideoIds = tag1.getVideoIds();
                    oldVideoIds.add(videoId);
                }).log();
    }
}
