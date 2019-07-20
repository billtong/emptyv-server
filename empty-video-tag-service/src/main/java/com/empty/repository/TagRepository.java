package com.empty.repository;

import com.empty.domain.Tag;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TagRepository extends ReactiveMongoRepository<Tag, String> {

}

