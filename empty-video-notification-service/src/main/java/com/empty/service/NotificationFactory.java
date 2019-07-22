package com.empty.service;

import com.empty.domain.Notification;
import com.empty.domain.Operations;
import com.empty.repository.NotificationRepository;
import com.empty.service.product.NotificationProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class NotificationFactory {

    @Autowired
    NotificationRepository notificationRepository;

    @Resource(name = "comment-notification-product")
    NotificationProduct commentNotificationProduct;

    public Mono<Notification> process(Map<String, Object> operationMessage) {
        String userId = String.valueOf(operationMessage.get("userId"));
        String operation = String.valueOf(operationMessage.get("operation"));
        Map objectMap = (Map) operationMessage.get("object");
        switch (Operations.valueOf(operation)) {
            case WRITE_A_COMMENT:
                return commentNotificationProduct.createCommentNotification(userId, operation, objectMap);
            case LIKE_A_COMMENT:
                return commentNotificationProduct.likeCommentNotification(userId, operation, objectMap);
            case DELETE_A_COMMENT:
                break;
        }
        return Mono.empty();
    }
}
