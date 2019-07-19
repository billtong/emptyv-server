package com.empty.broker;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class KafkaMessageListener {


    @KafkaListener(groupId = "ev-consumer", topics = {"notification"})
    public void notificationListen(@Payload Map<String, Object> notification, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("noti--接收消息: {}，partition：{}", notification, partition);
    }

    /*
    @KafkaListener(groupId = "ev-consumer", topics = {"history"})
    public void historyListen(@Payload Map<String, String> notification, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("hist--接收消息: {}，partition：{}", notification, partition);
    }
    */



}