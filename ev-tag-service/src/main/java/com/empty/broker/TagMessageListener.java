package com.empty.broker;

import com.empty.repository.TagRepository;
import com.empty.service.TagFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class TagMessageListener {
    @Autowired
    KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    @Autowired
    TagFactory tagFactory;
    @Autowired
    TagRepository tagRepository;

    @KafkaListener(groupId = "ev-consumer", topics = {"video-tag"})
    public void notificationListen(@Payload Map<String, Object> tagMap, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info(tagMap.toString());
        tagFactory.process(tagMap).subscribe(tag -> tagRepository.save(tag).subscribe());
    }
}
