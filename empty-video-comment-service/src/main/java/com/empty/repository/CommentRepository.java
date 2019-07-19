package com.empty.repository;

import com.empty.domain.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findAllByVideoIdOrderByCreatedDesc(String videoId);

    Flux<Comment> findAllByVideoIdOrderByLikeNumDesc(String videoId);

    Flux<Comment> findAllByVideoIdOrderByReplyNumDesc(String videoId);

}
