package com.empty.broker;

import com.empty.repository.VideoRepository;
import com.empty.service.VideoCountFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class VideoMessageListener {
    @Autowired
    KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    @Autowired
    VideoCountFactory videoCountFactory;
    @Autowired
    VideoRepository videoRepository;

    @KafkaListener(groupId = "ev-consumer", topics = {"video-count"})
    public void notificationListen(@Payload Map<String, Object> countMap, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info(countMap.toString());
        videoCountFactory.process(countMap).subscribe(video -> {
            if (video != null) {
                videoRepository.save(video).subscribe();
            }
        });
    }
}
