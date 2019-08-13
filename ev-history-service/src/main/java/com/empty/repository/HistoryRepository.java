package com.empty.repository;

import com.empty.domain.History;
import com.empty.domain.OperationEnum;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface HistoryRepository extends ReactiveMongoRepository<History, String> {

    Flux<History> findAllByUserIdOrderByCreatedDesc(String userId);

    Flux<History> findAllByUserIdAndOperation(String userId, OperationEnum operation);

}
