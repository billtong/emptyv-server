package com.empty.repository;

import com.empty.domain.Dan;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface DanRepository extends ReactiveMongoRepository<Dan, String> {
    Flux<Dan> findAllByVideoIdOrderByVideoTimeAsc(String videoId);
}
