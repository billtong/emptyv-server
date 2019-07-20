package com.empty.repository;

import com.empty.domain.FavList;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FavListRepository extends ReactiveMongoRepository<FavList, String> {
}
