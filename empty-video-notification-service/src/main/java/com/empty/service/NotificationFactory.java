package com.empty.service;

import com.empty.domain.Notification;
import com.empty.domain.OperationEnum;
import com.empty.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class NotificationFactory {
    @Autowired
    NotificationProduct notificationProduct;

    public Mono<Notification> process(Map<String, Object> operationMessage) {
        String userId = String.valueOf(operationMessage.get("userId"));
        String operation = String.valueOf(operationMessage.get("operation"));
        Map objectMap = (Map) operationMessage.get("object");
        switch (OperationEnum.valueOf(operation)) {
            case LOGIN:
                break;
            case UPDATE_A_USER:
                return notificationProduct.userUpdateOrActivateNotification(userId, operation);
            case ACTIVATE_A_USER:
                return notificationProduct.userUpdateOrActivateNotification(userId, operation);
            case WRITE_A_COMMENT:
                return notificationProduct.createCommentNotification(userId, operation, objectMap);
            case LIKE_A_COMMENT:
                return notificationProduct.likeCommentNotification(userId, operation, objectMap);
            case DELETE_A_COMMENT:
                break;
        }
        return Mono.empty();
    }
}
