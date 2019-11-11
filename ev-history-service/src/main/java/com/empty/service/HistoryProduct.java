package com.empty.service;

import com.empty.domain.History;
import com.empty.domain.OperationEnum;
import com.empty.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;

@Component
public class HistoryProduct {
    @Autowired
    HistoryRepository historyRepository;

    public Mono<History> createHistory(String userId, OperationEnum operation, Map objectMap) {
        History history = new History(userId, operation, objectMap);
        return Mono.just(history);
    }

    public Mono<History> removeHistory(String userId, OperationEnum rmOperation, Map objectMap) {
        return historyRepository.findAllByUserIdAndOperation(userId, rmOperation)
                .filter(history -> history.getObject().get("id").equals(objectMap.get("id")))
                .flatMap(history -> historyRepository.deleteById(history.getId()))
                .then(Mono.empty());
    }

    public Mono<History> createUniqueHistory(String userId, OperationEnum operationEnum, Map objectMap) {
        return historyRepository.findAllByUserIdAndOperation(userId, operationEnum)
                .filter(history -> history.getObject().get("id").equals(objectMap.get("id")))
                .collectList()
                .flatMap(histories -> {
                    if (histories.isEmpty()) {
                        return Mono.just(new History(userId, operationEnum, objectMap));
                    } else {
                        History history = histories.get(0);
                        history.setCreated(new Date());
                        return Mono.just(history);
                    }
                });
    }
}
