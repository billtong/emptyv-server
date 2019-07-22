package com.empty.broker;


import com.empty.repository.NotificationRepository;
import com.empty.service.NotificationFactory;
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
public class MessageListener {

    @Autowired
    NotificationFactory notificationFactory;

    @Autowired
    NotificationRepository notificationRepository;

    @KafkaListener(groupId = "ev-consumer", topics = {"operation"})
    public void notificationListen(@Payload Map<String, Object> operationMap, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        String field = String.valueOf(operationMap.get("field"));
        notificationFactory.process(operationMap).subscribe(notification -> {
            notificationRepository.save(notification).subscribe();
        });
    }
    /*
    @KafkaListener(groupId = "ev-consumer", topics = {"history"})
    public void historyListen(@Payload Map<String, String> notification, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("hist--接收消息: {}，partition：{}", notification, partition);
    }
    */
}