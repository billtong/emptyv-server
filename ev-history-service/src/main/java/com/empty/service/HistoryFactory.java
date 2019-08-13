package com.empty.service;

import com.empty.domain.History;
import com.empty.domain.OperationEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
            return historyProduct.createHistory(userId, operationEnum, null);
        } else if (operationEnum == VIEW_A_VIDEO || operationEnum == LIKE_A_COMMENT || operationEnum == UNLIKE_A_VIDEO || operationEnum == LIKE_A_VIDEO) {
            return historyProduct.createUniqueHistory(userId, operationEnum, (Map) operationMap.get("object"));
        } else if (operationEnum == CANCEL_LIKE_A_COMMENT) {
            return historyProduct.removeHistory(userId, LIKE_A_COMMENT, (Map) operationMap.get("object"));
        } else if (operationEnum == CANCEL_LIKE_A_VIDEO) {
            return historyProduct.removeHistory(userId, LIKE_A_VIDEO, (Map) operationMap.get("object"));
        } else if (operationEnum == CANCEL_UNLIKE_A_VIDEO) {
            return historyProduct.removeHistory(userId, UNLIKE_A_VIDEO, (Map) operationMap.get("object"));
        } else {
            return Mono.empty();
        }
    }
}

//{videoId=test, userId=5d3113ffe42cc92f407aeda6, operation=FAV_A_VIDEO, object={id=5d524be71480073f382ab719, userId=5d3113ffe42cc92f407aeda6, created=1565674471002, name=test fav list, updated=null, description=null, videoIds=[test], deleted=false, public=true}}
//{videoId=test, userId=5d3113ffe42cc92f407aeda6, operation=CANCEL_FAV_A_VIDEO, object={id=5d524be71480073f382ab719, userId=5d3113ffe42cc92f407aeda6, created=1565674471002, name=test fav list, updated=1565674519923, description=null, videoIds=[], deleted=false, public=true}}