package com.agateram.emptyvideo.repository;

import com.agateram.emptyvideo.domain.OAuthClient;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface OAuthClientRepository extends ReactiveMongoRepository<OAuthClient, String> {
}
