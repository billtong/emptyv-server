package com.empty.repository;

import com.empty.domain.Tag;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface TagRepository extends ReactiveMongoRepository<Tag, String> {
    Mono<Tag> findByName(String name);
}

