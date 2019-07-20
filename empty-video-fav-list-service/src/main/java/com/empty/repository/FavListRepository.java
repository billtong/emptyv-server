package com.empty.repository;

import com.empty.domain.FavList;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface FavListRepository extends ReactiveMongoRepository<FavList, String> {
    Flux<FavList> findAllByUserIdOrderByCreatedAsc(String userId);
}
