package com.empty.service.factory.concrete;

import com.empty.service.factory.NotificationProduct;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(value = "message")
public class MessageNotification implements NotificationProduct {
    @Override
    public void create(Map<String, Object> content) {

    }
}
