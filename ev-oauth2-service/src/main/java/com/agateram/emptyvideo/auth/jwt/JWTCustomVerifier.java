package com.agateram.emptyvideo.auth.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.function.Predicate;

public class JWTCustomVerifier {
    private JWSVerifier jwsVerifier;
    private Predicate<SignedJWT> isNotExpired = token ->
            getExpirationDate(token).after(Date.from(Instant.now()));
    private Predicate<SignedJWT> validSignature = token -> {
        try {
            return token.verify(this.jwsVerifier);
        } catch (JOSEException e) {
            e.printStackTrace();
            return false;
        }
    };

    public JWTCustomVerifier() {
        this.jwsVerifier = this.buildJWSVerifier();
    }

    public Mono<SignedJWT> check(String token) {
        return Mono.justOrEmpty(createJWS(token))
                .filter(isNotExpired)
                .filter(validSignature);
    }

    private MACVerifier buildJWSVerifier() {
        try {
            return new MACVerifier(JWTSecrets.DEFAULT_SECRET);
        } catch (JOSEException e) {
            e.printStackTrace();
            return null;
        }
    }

    private SignedJWT createJWS(String token) {
        try {
            return SignedJWT.parse(token);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date getExpirationDate(SignedJWT token) {
        try {
            return token.getJWTClaimsSet()
                    .getExpirationTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
