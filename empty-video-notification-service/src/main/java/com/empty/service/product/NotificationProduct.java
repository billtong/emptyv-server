package com.empty.service.product;

import com.empty.domain.Notification;
import com.empty.domain.Operations;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface NotificationProduct {
    Mono<Notification> createCommentNotification(String userId, String operations, Map object);

    Mono<Notification> likeCommentNotification(String userId, String operation, Map objectMap);
}
