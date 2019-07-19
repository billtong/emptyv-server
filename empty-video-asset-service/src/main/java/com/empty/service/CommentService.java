package com.empty.service;

import com.empty.domain.Comment;
import com.empty.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Slf4j
@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        String commentId = serverRequest.pathVariable("id");
        return commentRepository.findById(String.valueOf(commentId)).flatMap(
                comment -> ok().body(Mono.just(comment), Comment.class)).switchIfEmpty(notFound().build());
    }

    //将comment replies转换移动到前端完成，后端并发写不了。
    public Mono<ServerResponse> getByVideoId(ServerRequest serverRequest) {
        String videoId = serverRequest.pathVariable("id");
        String sort = String.valueOf(serverRequest.queryParam("sort"));
        return commentRepository.findAllByVideoIdOrderByCreatedDesc(videoId).collectList().flatMap(commentList ->
                ok().body(Mono.just(commentList), List.class));
    }

    // post one comment + change the parent reply num
    public Mono<ServerResponse> write(ServerRequest serverRequest) {
        Comment comment = (Comment) serverRequest.attribute("comment").get();
        if (comment.getParentId() != null) {
            commentRepository.findById(comment.getParentId()).subscribe(comment1 -> {
                comment1.setReplyNum(comment1.getReplyNum() + 1);
                commentRepository.save(comment1).subscribe();
            });
        }
        Mono<Comment> savedMono = commentRepository.save(comment);
        return Mono.zip(savedMono, Mono.just(serverRequest)).flatMap(tuple -> {
            Comment savedComment = tuple.getT1();
            tuple.getT2().attributes().put("comment", savedComment);
            return status(201).body(Mono.just(savedComment), Comment.class);
        });
    }

    public Mono<ServerResponse> likeCommentById(ServerRequest req) {
        String commentId = req.pathVariable("id");
        Mono<Comment> commentMono = commentRepository.findById(commentId);
        return Mono.zip(Mono.just(req), commentMono).flatMap(tuple -> {
            ServerRequest req2 = tuple.getT1();
            Comment comment = tuple.getT2();
            comment.setLikeNum(comment.getLikeNum() + 1);
            req2.attributes().put("comment", comment);
            return commentRepository.save(comment).then(ok().build());
        });
    }

    public Mono<ServerResponse> deleteById(ServerRequest req) {
        String commentId = req.pathVariable("id");
        Mono<Comment> commentMono = commentRepository.findById(commentId);
        return Mono.zip(Mono.just(req), commentMono).flatMap(tuple -> {
            ServerRequest req2 = tuple.getT1();
            Comment comment = tuple.getT2();
            comment.setDeleted(true);
            req2.attributes().put("comment", comment);
            return commentRepository.save(comment).then(ok().build());
        });
    }
}
