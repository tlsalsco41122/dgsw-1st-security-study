package com.kr.hs.dgsw_security.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class JwtFactory {
    private String subject = "spring@dgsw.hs.kr";
    private Date issuedAt = new Date();
    private Date expiresAt = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
    private Map<String, Object> claims = Collections.emptyMap();

    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiresAt, Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiresAt = expiresAt != null ? expiresAt : this.expiresAt;
        this.claims = claims != null ? claims : this.claims;
    }

    // 기본값으로 토큰 생성해서 사용할 떄
    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build();
    }

    public String createToken(JwtProperties jwtProperties) {
        SecretKey key = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtProperties.getSecretKey())
        );

        return Jwts.builder()
                .header().type("JWT")
                .and() // end header
                .issuer(jwtProperties.getIssuer())
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .subject(subject)
                .claims().add(claims)
                .and() // end payload
                .signWith(key)
                .compact();
    }
}
