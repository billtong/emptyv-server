package com.empty.broker;

import com.empty.domain.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserMessageProvider {

    @Autowired
    KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public void sendMessageProvider(Notification notification) {
        Map<String, Object> map = new HashMap<>();
        map.put("notificationId", notification.getId());
        map.put("userId", notification.getTo());
        this.kafkaTemplate.send("user-update", map);
    }
}
