package com.empty.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class UserWebClient {

    private final WebClient webClient;
    private final String userClientBaseUrl = "http://localhost:8001";

    public UserWebClient() {
        this.webClient = WebClient.builder().baseUrl(userClientBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    public Mono<ClientResponse> getUserByAuthToken(ServerRequest serverRequest) {
        List<String> authList = serverRequest.headers().header(HttpHeaders.AUTHORIZATION);
        String bearerToken = authList.isEmpty() ? null : authList.get(0);
        return this.webClient.get()
                .uri("/auth/user")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .exchange();
    }
}

