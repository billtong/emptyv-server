package com.empty.repository;

import com.empty.domain.Video;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VideoRepository extends ReactiveMongoRepository<Video, String> {

}
