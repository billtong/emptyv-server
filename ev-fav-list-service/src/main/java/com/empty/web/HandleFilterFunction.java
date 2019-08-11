package com.empty.web;

import com.empty.domain.FavList;
import com.empty.domain.OperationEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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

import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Slf4j
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

    public Mono<ServerResponse> msgProduceAfterFilterFunction(ServerRequest req, HandlerFunction<ServerResponse> next) {
        Mono<ServerResponse> res = next.handle(req);
        return Mono.zip(res, Mono.just(req)).flatMap(tuple1 -> {
            ServerResponse result = tuple1.getT1();
            if (result.statusCode().is2xxSuccessful()) {
                ServerRequest req2 = tuple1.getT2();
                String authId = String.valueOf(req2.attribute("authId").get());
                FavList favList = (FavList) req2.attribute("favList").get();
                Map<String, Object> map = new HashMap<>();
                map.put("userId", authId);
                map.put("object", favList);
                HttpMethod method = Objects.requireNonNull(req2.method());
                if (method.equals(POST) && !favList.getVideoIds().isEmpty()) {
                    String videoId = favList.getVideoIds().get(0);
                    map.put("operation", OperationEnum.FAV_A_VIDEO);
                    map.put("videoId", videoId);
                } else if (method.equals(PATCH)) {
                    map.put("operation", req2.queryParam("operation").get());
                    map.put("videoId", req2.queryParam("videoId").get());
                } else {
                    return Mono.just(result);
                }
                ListenableFuture<SendResult<String, Map<String, Object>>> historyFuture = this.kafkaTemplate.send("history", map);
                ListenableFuture<SendResult<String, Map<String, Object>>> pointFuture = this.kafkaTemplate.send("point", map);
                ListenableFuture<SendResult<String, Map<String, Object>>> videoCountFuture = this.kafkaTemplate.send("video-count", map);
                return Mono.zip(Mono.fromFuture(historyFuture.completable()), Mono.fromFuture(pointFuture.completable()), Mono.fromFuture(videoCountFuture.completable()))
                        .then(Mono.just(result));
            } else {
                return Mono.just(result);
            }
        });
    }
}
