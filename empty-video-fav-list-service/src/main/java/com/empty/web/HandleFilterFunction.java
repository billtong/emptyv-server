package com.empty.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Slf4j
@Component
public class HandleFilterFunction {
    @Autowired
    UserWebClient userWebClient;

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
        log.info("produce messaage to notification");
        return res;
    }
}
