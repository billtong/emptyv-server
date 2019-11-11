package com.empty.repository;

import com.empty.domain.Notification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface NotificationRepository extends ReactiveMongoRepository<Notification, String> {
    public Flux<Notification> findAllByToOrderByCreatedDesc(String to);
}
