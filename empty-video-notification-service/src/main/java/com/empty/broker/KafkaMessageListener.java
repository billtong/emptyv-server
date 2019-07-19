package com.empty.broker;


import com.empty.service.factory.NotificationFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class KafkaMessageListener {

    @Autowired
    NotificationFactory notificationFactory;

    @KafkaListener(groupId = "ev-consumer", topics = {"notification"})
    public void notificationListen(@Payload Map<String, Object> notification, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        String field = String.valueOf(notification.get("field"));
        notificationFactory.getNotificationProduct(field).create(notification);
    }

    /*
    @KafkaListener(groupId = "ev-consumer", topics = {"history"})
    public void historyListen(@Payload Map<String, String> notification, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("hist--接收消息: {}，partition：{}", notification, partition);
    }
    */



}