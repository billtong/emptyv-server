package com.agateram.emptyvideo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;

@EnableDiscoveryClient
@SpringBootApplication
public class EvApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(EvApiGatewayApplication.class, args);
    }
}

@Configuration
class CorsConfig {
    private static final long MAX_AGE = 180000L;

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = exchange.getResponse();
                HttpHeaders responseHeaders = response.getHeaders();
                responseHeaders.setAccessControlAllowCredentials(true);
                responseHeaders.setAccessControlAllowOrigin("http://localhost:3000");
                responseHeaders.setAccessControlExposeHeaders(Collections.singletonList("Authorization"));
                responseHeaders.setAccessControlMaxAge(MAX_AGE);
                responseHeaders.setAccessControlAllowHeaders(Arrays.asList("authorization", "content-type"));
                responseHeaders.setAccessControlAllowMethods(Arrays.asList(HttpMethod.GET, HttpMethod.PATCH, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.OPTIONS));
            }
            return chain.filter(exchange);
        };
    }

    @Bean
    public ServerCodecConfigurer serverCodecConfigurer() {
        return new DefaultServerCodecConfigurer();
    }

    @Bean
    public RouteDefinitionLocator routeDefinitionLocator(DiscoveryClient discoveryClient, DiscoveryLocatorProperties properties) {
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient, properties);
    }

}
