package com.empty.broker;

import com.empty.repository.HistoryRepository;
import com.empty.service.HistoryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class HistoryMessageListener {

    @Autowired
    HistoryFactory historyFactory;
    @Autowired
    HistoryRepository historyRepository;

    @KafkaListener(groupId = "ev-consumer", topics = {"history"})
    public void notificationListen(
            @Payload Map<String, Object> operationMap,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        historyFactory.process(operationMap).subscribe(history -> {
            if (history != null) {
                historyRepository.save(history).subscribe();
            }
        });
    }
}


