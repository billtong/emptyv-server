package com.empty.web;

import com.empty.domain.OperationEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpMethod.*;

@Component
@Slf4j
public class HandleFilterFunction {

    @Autowired
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public Mono<ServerResponse> userAfterFilterHandle(ServerRequest req, HandlerFunction<ServerResponse> next) {
        Mono<ServerResponse> responseMono = next.handle(req);
        return Mono.zip(responseMono, Mono.just(req)).flatMap(tuple -> {
            ServerResponse serverResponse = tuple.getT1();
            if (serverResponse.statusCode().is2xxSuccessful()) {
                ServerRequest req2 = tuple.getT2();
                String userId = String.valueOf(req2.attribute("userId").get());
                Map<String, Object> map = new HashMap<>();
                map.put("userId", userId);
                if (Objects.requireNonNull(req2.method()).equals(GET)) {    //matches not working here
                    map.put("operation", OperationEnum.ACTIVATE_A_USER);
                } else if (Objects.requireNonNull(req2.method()).equals(PATCH) && req2.path().equals("/api/user")) {
                    map.put("operation", OperationEnum.UPDATE_A_USER);
                } else if (Objects.requireNonNull(req2.method()).equals(POST) && req2.path().equals("/auth/login")) {
                map.put("operation", OperationEnum.LOGIN);
                ListenableFuture<SendResult<String, Map<String, Object>>> historyFuture = this.kafkaTemplate.send("history", map);
                ListenableFuture<SendResult<String, Map<String, Object>>> pointFuture = this.kafkaTemplate.send("point", map);
                return Mono.zip(Mono.fromFuture(historyFuture.completable()), Mono.fromFuture(pointFuture.completable()))
                        .then(Mono.just(serverResponse));
                }
                ListenableFuture<SendResult<String, Map<String, Object>>> notificationFuture = this.kafkaTemplate.send("notification", map);
                ListenableFuture<SendResult<String, Map<String, Object>>> historyFuture = this.kafkaTemplate.send("history", map);
                ListenableFuture<SendResult<String, Map<String, Object>>> pointFuture = this.kafkaTemplate.send("point", map);
                return Mono.zip(Mono.fromFuture(notificationFuture.completable()), Mono.fromFuture(historyFuture.completable()), Mono.fromFuture(pointFuture.completable()))
                        .then(Mono.just(serverResponse));
            } else {
                return Mono.just(serverResponse);
            }
        });
    }
}
