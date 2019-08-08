package com.agateram.emptyvideo.auth.jwt;

import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class UsernamePasswordAuthenticationBearer {
    public static Mono<Authentication> create(SignedJWT signedJWTMono) {
        String subject;
        String auths;
        List authorities;

        try {
            subject = signedJWTMono.getJWTClaimsSet().getSubject();
            auths = (String) signedJWTMono.getJWTClaimsSet().getClaim("roles");
        } catch (ParseException e) {
            return Mono.empty();
        }
        authorities = Stream.of(auths.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(subject, null, authorities));
    }
}
