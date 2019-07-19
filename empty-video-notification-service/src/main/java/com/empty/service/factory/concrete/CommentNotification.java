package com.empty.service.factory.concrete;

import com.empty.broker.UserMessageProvider;
import com.empty.domain.Notification;
import com.empty.repository.NotificationRepository;
import com.empty.service.factory.NotificationProduct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component(value = "comment")
public class CommentNotification implements NotificationProduct {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserMessageProvider userMessageProvider;

    @Override
    public void create(Map<String, Object> content) {
        String action = String.valueOf(content.get("action"));
        String subjectId = String.valueOf(content.get("subject"));
        Map comment = (Map) content.get("object");
        String commentId = String.valueOf(comment.get("id"));
        String commentUserId = String.valueOf(comment.get("userId"));
        String commentAtId = String.valueOf(comment.get("at"));

        Map<String, String> notiContent = new HashMap<>();
        notiContent.put("field", "comment");
        notiContent.put("subject", subjectId);
        notiContent.put("object", commentId);
        notiContent.put("action", action);

        Notification n2 = new Notification(subjectId, notiContent);
        notificationRepository.save(n2).subscribe(userMessageProvider::sendMessageProvider);
        if (!commentUserId.equals(subjectId)) {
            Notification n1 = new Notification(commentUserId, notiContent);
            notificationRepository.save(n1).subscribe(userMessageProvider::sendMessageProvider);
        }
        if (!commentAtId.isEmpty() && !commentAtId.equals(subjectId)) {
            Notification n1 = new Notification(commentAtId, notiContent);
            notificationRepository.save(n1).subscribe(userMessageProvider::sendMessageProvider);
        }
    }
}
