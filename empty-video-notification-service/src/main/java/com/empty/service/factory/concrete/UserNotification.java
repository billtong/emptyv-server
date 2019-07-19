package com.empty.service.factory.concrete;

import com.empty.service.factory.NotificationProduct;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(value = "user")
public class UserNotification implements NotificationProduct {
    @Override
    public void create(Map<String, Object> content) {

    }
}
