package com.empty.service;

import com.empty.domain.Notification;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationProduct {
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

    public Mono<Notification> likeCommentNotification(String userId, String operation, Map objectMap) {
        String commentId = (String) objectMap.get("id");
        String to = (String) objectMap.get("userId");
        Map<String, String> content = new HashMap<>();
        content.put("userId", userId);
        content.put("operation", operation);
        content.put("commentId", commentId);
        Notification notification = new Notification(to, content);
        return Mono.just(notification);
    }

    public Mono<Notification> userUpdateOrActivateNotification(String userId, String operation) {
        Map<String, String> content = new HashMap<>();
        content.put("userId", userId);
        content.put("operation", operation);
        Notification notification = new Notification(userId, content);
        return Mono.just(notification);
    }
}
