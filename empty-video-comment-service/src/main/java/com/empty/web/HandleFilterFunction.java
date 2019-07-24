package com.empty.web;

import com.empty.domain.Comment;
import com.empty.domain.OperationEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Slf4j
@Component
public class HandleFilterFunction {
    @Autowired
    UserWebClient userWebClient;

    @Autowired
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public Mono<ServerResponse> authCheckBeforeFilterFunction(ServerRequest req, HandlerFunction<ServerResponse> next) {
        Mono<ClientResponse> clientResponseMono = userWebClient.getUserByAuthToken(req);
        return Mono.zip(Mono.just(req), Mono.just(next), clientResponseMono).flatMap(tuple1 -> {
            ServerRequest req2 = tuple1.getT1();
            HandlerFunction<ServerResponse> next2 = tuple1.getT2();
            ClientResponse clientResponse = tuple1.getT3();
            if (clientResponse.statusCode().is2xxSuccessful()) {
                Mono<Map> userMapMono = clientResponse.bodyToMono(Map.class);
                switch (req2.method()) {
                    case POST:
                        Mono<Comment> commentMono = req2.bodyToMono(Comment.class);
                        return Mono.zip(userMapMono, commentMono, Mono.just(req2), Mono.just(next2)).flatMap(tuple2 -> {
                            Map<String, String> userMap = tuple2.getT1();
                            Comment comment = tuple2.getT2();
                            ServerRequest req3 = tuple2.getT3();
                            HandlerFunction<ServerResponse> next3 = tuple2.getT4();
                            comment.setUserId(userMap.get("id"));
                            req3.attributes().put("comment", comment);
                            req3.attributes().put("authId", userMap.get("id"));
                            return next3.handle(req3);
                        });
                    case PATCH:
                    case DELETE:
                }
                return Mono.zip(Mono.just(next2), Mono.just(req2), userMapMono).flatMap(tuple3 -> {
                    Map<String, String> userMap = tuple3.getT3();
                    ServerRequest serverRequest = tuple3.getT2();
                    serverRequest.attributes().put("authId", userMap.get("id"));
                    return tuple3.getT1().handle(serverRequest);
                });
            } else {
                return status(HttpStatus.FORBIDDEN).build();
            }
        });
    }

    public Mono<ServerResponse> commentAfterFilterFunction(ServerRequest req, HandlerFunction<ServerResponse> next) {
        Mono<ServerResponse> serverRequestMono = next.handle(req);
        return Mono.zip(serverRequestMono, Mono.just(req)).flatMap(tuple -> {
            ServerResponse serverResponse = tuple.getT1();
            if (serverResponse.statusCode().is2xxSuccessful()) {
                ServerRequest req2 = tuple.getT2();
                String authId = String.valueOf(req2.attribute("authId").get());
                Comment comment = (Comment) req2.attribute("comment").get();
                Map<String, Object> map = new HashMap<>();
                map.put("userId", authId);
                map.put("object", comment);
                if (Objects.requireNonNull(req2.method()).equals(POST) && req2.path().equals("/api/comment")) {
                    map.put("operation", OperationEnum.WRITE_A_COMMENT);
                } else if (Objects.requireNonNull(req2.method()).equals(PATCH) && req2.path().equals("/api/comment/".concat(comment.getId()).concat("/like"))) {
                    map.put("operation", OperationEnum.LIKE_A_COMMENT);
                } else if (Objects.requireNonNull(req2.method()).equals(DELETE) && req2.path().equals("/api/comment/".concat(comment.getId()))) {
                    map.put("operation", OperationEnum.DELETE_A_COMMENT);
                    ListenableFuture<SendResult<String, Map<String, Object>>> historyFuture = this.kafkaTemplate.send("history", map);
                    return Mono.fromFuture(historyFuture.completable()).then(Mono.just(serverResponse));
                } else {
                    return Mono.just(serverResponse);
                }
                ListenableFuture<SendResult<String, Map<String, Object>>> notificationFuture = this.kafkaTemplate.send("notification", map);
                ListenableFuture<SendResult<String, Map<String, Object>>> historyFuture = this.kafkaTemplate.send("history", map);
                ListenableFuture<SendResult<String, Map<String, Object>>> pointFuture = this.kafkaTemplate.send("point", map);
                return Mono.zip(Mono.fromFuture(notificationFuture.completable()), Mono.fromFuture(historyFuture.completable()), Mono.fromFuture(pointFuture.completable()))
                        .then(Mono.just(serverResponse));
        }
            return Mono.just(serverResponse);
        });
    }
}
