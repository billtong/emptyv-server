package com.empty.filter;

import com.empty.client.UserWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class HandleCommentFilterFunction {
    @Autowired
    UserWebClient userWebClient;

    // check if the authUserId == reqeustUserId
    public Mono<ServerResponse> authCheckFilterFunction(ServerRequest req, HandlerFunction<ServerResponse> next) {
        Mono<ClientResponse> clientResponseMono = userWebClient.getUserByAuthToken(req);
        return Mono.zip(Mono.just(req), Mono.just(next), clientResponseMono).flatMap(tuple1 -> {
            ServerRequest req2 = tuple1.getT1();
            HandlerFunction<ServerResponse> next2 = tuple1.getT2();
            ClientResponse clientResponse = tuple1.getT3();
            if (clientResponse.statusCode().is2xxSuccessful()) {
                Mono<Map> userMapMono = clientResponse.bodyToMono(Map.class);
                return Mono.zip(Mono.just(req2), Mono.just(next2), userMapMono).flatMap(tuple2 -> {
                    ServerRequest req3 = tuple2.getT1();
                    HandlerFunction<ServerResponse> next3 = tuple2.getT2();
                    Map<String, String> userMap = tuple2.getT3();
                    String authUserId = userMap.get("id");
                    req3.attributes().put("userId", authUserId);
                    return next3.handle(req3);
                });
            } else {
                return ServerResponse.status(HttpStatus.FORBIDDEN).build();
            }
        });
    }
}
