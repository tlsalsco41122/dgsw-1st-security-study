package com.kr.hs.dgsw_security.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties("jwt") // application.yml 파일의 프로퍼티 값을 가지고 자바 객체로 만들어 주는 놈.
public class JwtProperties {
    private String issuer;
    private String secretKey;
    private String accessExpirationMinutes;
    private String refreshExpirationMinutes;
}
