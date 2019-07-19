package com.empty.web;

import com.empty.web.UserWebClient;
import com.empty.domain.Comment;
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

import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Slf4j
@Component
public class HandleFilterFunction {
    @Autowired
    UserWebClient userWebClient;

    @Autowired
    private KafkaTemplate<String, Map<String,Object>> kafkaTemplate;

    public Mono<ServerResponse> authCheckBeforeFilterFunction(ServerRequest req, HandlerFunction<ServerResponse> next) {
        Mono<ClientResponse> clientResponseMono = userWebClient.getUserByAuthToken(req);
        return Mono.zip(Mono.just(req), Mono.just(next), clientResponseMono).flatMap(tuple1 -> {
            ServerRequest req2 = tuple1.getT1();
            HandlerFunction<ServerResponse> next2 = tuple1.getT2();
            ClientResponse clientResponse = tuple1.getT3();
            if (clientResponse.statusCode().is2xxSuccessful()) {
                switch (req2.path()) {
                    case "/api/comment":
                        Mono<Map> userMapMono = clientResponse.bodyToMono(Map.class);
                        Mono<Comment> commentMono = req2.bodyToMono(Comment.class);
                        return Mono.zip(userMapMono, commentMono, Mono.just(req2), Mono.just(next2)).flatMap(tuple2 -> {
                            Map<String, String> userMap = tuple2.getT1();
                            Comment comment = tuple2.getT2();
                            ServerRequest req3 = tuple2.getT3();
                            HandlerFunction<ServerResponse> next3 = tuple2.getT4();
                            comment.setUserId(userMap.get("id"));
                            req3.attributes().put("comment", comment);
                            return next3.handle(req3);
                        });
                    default:
                        return next2.handle(req2);
                }
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

                switch (Objects.requireNonNull(req2.method())) {
                    case POST:
                        Comment comment = (Comment) req2.attribute("comment").get();
                        log.info(comment.toString());
                        Map<String, Object> map = new HashMap<>();
                        map.put("field", "comment");
                        map.put("action", "create");
                        map.put("comment", comment);
                        ListenableFuture<SendResult<String, Map<String, Object>>> notificationFuture = this.kafkaTemplate.send("notification", map);
                        return Mono.fromFuture(notificationFuture.completable()).then(Mono.just(serverResponse));
                    case PATCH:

                    case DELETE:
                        default:
                            return Mono.just(serverResponse);
                }
            }
            return status(400).build();
        });
    }
}
