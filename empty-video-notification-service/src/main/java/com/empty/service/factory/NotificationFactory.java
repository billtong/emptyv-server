package com.empty.service.factory;

import com.empty.domain.Notification;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class NotificationFactory {
    private final String COMMENT = "comment";
    private final String MESSAGE = "message";
    private final String USER = "user";

    @Resource(name = "comment")
    NotificationProduct commentNotification;
    @Resource(name = "user")
    NotificationProduct userNotification;
    @Resource(name = "message")
    NotificationProduct messageNotification;

    public NotificationProduct getNotificationProduct(String field) {
        switch (field) {
            case COMMENT:
                return commentNotification;
            case USER:
                return userNotification;
            case MESSAGE:
                return messageNotification;
        }
        return null;
    }

}
