package com.empty.service;

import com.empty.domain.History;
import com.empty.domain.OperationEnum;
import org.apache.kafka.common.security.auth.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.empty.domain.OperationEnum.*;

@Component
public class HistoryFactory {
    @Autowired
    HistoryProduct historyProduct;

    public Mono<History> process(Map<String, Object> operationMap) {
        String userId = String.valueOf(operationMap.get("userId"));
        String operation = String.valueOf(operationMap.get("operation"));
        OperationEnum operationEnum = OperationEnum.valueOf(operation);
        if (operationEnum == LOGIN || operationEnum == ACTIVATE_A_USER || operationEnum == UPDATE_A_USER) {
            return historyProduct.defaultHistory(userId, operation, null);
        } else if (operationEnum == SEND_A_MESSAGE) {
            return Mono.empty();
        } else {
            return historyProduct.defaultHistory(userId, operation, (Map) operationMap.get("object"));
        }
    }
}
