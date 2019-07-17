package com.emptyvideo.ums.repository;

import com.emptyvideo.ums.domain.Session;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SessionRepository extends ReactiveMongoRepository<Session, String> {
    Mono<Session> findByUserId(String userId);
}
