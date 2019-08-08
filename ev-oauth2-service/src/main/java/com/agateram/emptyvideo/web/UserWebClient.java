package com.agateram.emptyvideo.web;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class UserWebClient {

    private final WebClient webClient;

    @Autowired
    public UserWebClient(EurekaClient eurekaClient) {
        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("user-service", false);
        String userClientBaseUrl = instanceInfo.getHomePageUrl();
        this.webClient = WebClient.builder().baseUrl(userClientBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<ClientResponse> getUserByAuthToken(String bearerToken) {
        return this.webClient.get()
                .uri("/auth/user")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .exchange().log();
    }

    public Mono<ClientResponse> getAuthTokenByLogin(String email, String pwd) {
        return this.webClient.post()
                .uri("/auth/login")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils
                        .encodeToString((email + ":" + pwd).getBytes(UTF_8)))
                .exchange().log();
    }

}
