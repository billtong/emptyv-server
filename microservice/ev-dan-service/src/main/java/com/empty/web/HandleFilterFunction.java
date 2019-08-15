package com.empty.web;

import com.empty.domain.Dan;
import com.empty.domain.OperationEnum;
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

import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
public class HandleFilterFunction {
    @Autowired
    UserWebClient userWebClient;
    @Autowired
    KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public Mono<ServerResponse> authCheckBeforeFilterFunction(ServerRequest req, HandlerFunction<ServerResponse> next) {
        Mono<ClientResponse> clientResponseMono = userWebClient.getUserByAuthToken(req);
        return Mono.zip(Mono.just(req), Mono.just(next), clientResponseMono).flatMap(tuple1 -> {
            ServerRequest req2 = tuple1.getT1();
            HandlerFunction<ServerResponse> next2 = tuple1.getT2();
            ClientResponse clientResponse = tuple1.getT3();
            if (clientResponse.statusCode().is2xxSuccessful()) {
                Mono<Map> userMapMono = clientResponse.bodyToMono(Map.class);
                return Mono.zip(userMapMono, Mono.just(req2), Mono.just(next2)).flatMap(tuple2 -> {
                    Map<String, String> userMap = tuple2.getT1();
                    ServerRequest req3 = tuple2.getT2();
                    HandlerFunction<ServerResponse> next3 = tuple2.getT3();
                    req3.attributes().put("authId", userMap.get("id"));
                    return next3.handle(req3);
                });
            } else {
                return status(HttpStatus.FORBIDDEN).build();
            }
        });
    }

    public Mono<ServerResponse> messageProvideAfterFilterFunction(ServerRequest req, HandlerFunction<ServerResponse> next) {
        Mono<ServerResponse> responseMono = next.handle(req);
        return Mono.zip(responseMono, Mono.just(req)).flatMap(tuple -> {
            ServerResponse response = tuple.getT1();
            if (response.statusCode().is2xxSuccessful()) {
                ServerRequest req2 = tuple.getT2();
                String authId = String.valueOf(req2.attribute("authId").get());
                Dan dan = (Dan) req2.attribute("dan").get();
                Map<String, Object> map = new HashMap<>();
                map.put("userId", authId);
                map.put("object", dan);
                map.put("operation", OperationEnum.WRITE_A_DAN);
                ListenableFuture<SendResult<String, Map<String, Object>>> historyFuture = this.kafkaTemplate.send("history", map);
                ListenableFuture<SendResult<String, Map<String, Object>>> pointFuture = this.kafkaTemplate.send("point", map);
                ListenableFuture<SendResult<String, Map<String, Object>>> videoCountFuture = this.kafkaTemplate.send("video-count", map);
                return Mono.zip(Mono.fromFuture(historyFuture.completable()), Mono.fromFuture(pointFuture.completable()), Mono.fromFuture(videoCountFuture.completable()))
                        .then(Mono.just(response));
            } else {
                return Mono.just(response);
            }
        });
    }
}
