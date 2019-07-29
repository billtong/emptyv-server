package com.empty.service;

import com.empty.domain.History;
import com.empty.domain.OperationEnum;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class HistoryProduct {

    public Mono<History> defaultHistory(String userId, String operation, Map objectMap) {
        History history = new History(userId, OperationEnum.valueOf(operation), objectMap);
        return Mono.just(history);
    }
}
