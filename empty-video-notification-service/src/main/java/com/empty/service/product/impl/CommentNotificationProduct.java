package com.empty.service.product.impl;

import com.empty.domain.Notification;
import com.empty.service.product.NotificationProduct;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component(value = "comment-notification-product")
public class CommentNotificationProduct implements NotificationProduct {
    @Override
    public Mono<Notification> createCommentNotification(String userId, String operation, Map objectMap) {
        Map commentMap = objectMap;
        String commentId = (String) commentMap.get("id");
        String to = (String) commentMap.get("at");
        if (to != null && !to.equals("")) {
            Map<String, String> content = new HashMap<>();
            content.put("userId", userId);
            content.put("operation", operation);
            content.put("commentId", commentId);
            Notification notification = new Notification(to, content);
            return Mono.just(notification);
        }
        return Mono.empty();
    }

    @Override
    public Mono<Notification> likeCommentNotification(String userId, String operation, Map objectMap) {
        Map commentMap = objectMap;
        String commentId = (String) commentMap.get("id");
        String to = (String) commentMap.get("userId");
        Map<String, String> content = new HashMap<>();
        content.put("userId", userId);
        content.put("operation", operation);
        content.put("commentId", commentId);
        Notification notification = new Notification(to, content);
        return Mono.just(notification);
    }
}
