package com.emptyvideo.ums.auth.bearer;


import com.emptyvideo.ums.auth.jwt.AuthorizationHeaderPayload;
import com.emptyvideo.ums.auth.jwt.JWTCustomVerifier;
import com.emptyvideo.ums.auth.jwt.UsernamePasswordAuthenticationBearer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {
    private static final String BEARER = "Bearer ";
    private static final Predicate<String> matchBearerLength = authValue -> authValue.length() > BEARER.length();
    private static final Function<String, Mono<String>> isolateBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(BEARER.length()));

    private JWTCustomVerifier jwtVerifier = new JWTCustomVerifier();

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange)
                .flatMap(AuthorizationHeaderPayload::extract)
                .filter(matchBearerLength)
                .flatMap(isolateBearerValue)
                .flatMap(jwtVerifier::check)
                .flatMap(UsernamePasswordAuthenticationBearer::create);
    }
}
