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
public class MessageListener {

    @KafkaListener(groupId = "ev-consumer", topics = {"user-update"})
    public void notificationListen(@Payload Map<String, Object> userUpdate, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info(userUpdate.toString());
        /**
         * send this info to the front end via websocket
         */
    }
}
