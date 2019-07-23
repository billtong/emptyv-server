package com.empty.web;

import com.empty.domain.OperationEnum;
import com.empty.domain.Video;
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
        log.info("send message to kafka");
        Mono<ServerResponse> result = next.handle(req);

        return Mono.zip(Mono.just(req), Mono.just(next), result).flatMap(tuple1 -> {
            ServerResponse response = tuple1.getT3();
            if (response.statusCode().is2xxSuccessful()) {
                ServerRequest req2 = tuple1.getT1();
                String userId = String.valueOf(req2.attribute("authId").get());
                Video video = (Video) req2.attributes().get("video");
                Map<String, Object> map = new HashMap<>();
                map.put("userId", userId);
                map.put("object", video);
                HttpMethod method = Objects.requireNonNull(req2.method());
                if (method.equals(POST) && req2.path().equals("/api/video")) {
                    map.put("operation", OperationEnum.POST_A_VIDEO);
                } else if (method.equals(PATCH)) {
                    String operation = String.valueOf(req2.queryParam("operation"));
                    map.put("operation", OperationEnum.valueOf(operation));
                }
                ListenableFuture<SendResult<String, Map<String, Object>>> notificationFuture = this.kafkaTemplate.send("operation", map);
                return Mono.fromFuture(notificationFuture.completable()).then(Mono.just(response));
            } else {
                return Mono.just(response);
            }
        });
    }
}
