package com.empty.auth.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JWTTokenService {

    public static String generateToken(String subject, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        SignedJWT signedJWT;
        JWTClaimsSet claimsSet;
        String issure = "emptyvideo.com";
        claimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(issure)
                .expirationTime(new Date(getExpiration()))
                .claim("roles", authorities
                        .stream()
                        .map(GrantedAuthority.class::cast)
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .build();

        signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        try {
            signedJWT.sign(new JWTCustomSigner().getSigner());
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        return signedJWT.serialize();
    }

    private static long getExpiration() {
        long expiration = 5;
        return new Date().toInstant()
                .plus(Duration.ofHours(expiration))
                .toEpochMilli();
    }
}
