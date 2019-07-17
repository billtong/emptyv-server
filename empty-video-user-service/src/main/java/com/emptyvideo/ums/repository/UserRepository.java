package com.emptyvideo.ums.repository;

import com.emptyvideo.ums.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findUserByEmail(String email);
}
