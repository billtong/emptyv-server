package com.emptyvideo.ums.auth.basic;

import com.emptyvideo.ums.auth.jwt.JWTTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class BasicAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        exchange.getResponse()
                .getHeaders()
                .add(HttpHeaders.AUTHORIZATION, getHttpAuthHeaderValue(authentication));
        return webFilterExchange.getChain().filter(exchange);
    }

    private String getHttpAuthHeaderValue(Authentication authentication) {
        return String.join(" ", "Bearer", tokenFromAuthentication(authentication));
    }

    private CharSequence tokenFromAuthentication(Authentication authentication) {
        return JWTTokenService.generateToken(authentication.getName(), authentication.getCredentials(), authentication.getAuthorities());
    }
}