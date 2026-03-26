package com.kr.hs.dgsw_security.config.jwt;

import com.kr.hs.dgsw_security.domain.User;
import com.kr.hs.dgsw_security.domain.UserRole;
import com.kr.hs.dgsw_security.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // 실제 서버처럼 테스트 진행, Bean 을 모두 객체
class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateBase64SecretKey() 비밀키가 정상적으로 생성 확인")
    @Test
    void generateBase64SecretKey() {
        SecretKey key = Jwts.SIG.HS256.key().build();
        byte[] encoded = key.getEncoded();

        assertThat(Encoders.BASE64.encode(encoded)).isNotEmpty();

    }

    @DisplayName("generateToken() 토큰 만들기")
    @Test
    void generateToken() {
        // given - 가상의 데이터가 있다.
        User testUser = userRepository.save(
                User.builder()
                        .email("spring@dgsw.hs.kr")
                        .password("spring")
                        .role(UserRole.USER)
                        .build());

        // when - 특정 상황을 만든다. 토큰 생성
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        // then
        // 디비 추가한 정보로 토큰을 생성
        // 토큰에서 유저정보 추출해서 디비유저랑 비교
        Long tokenId = Jwts.parser()
                .verifyWith(
                        Keys.hmacShaKeyFor(
                                Decoders.BASE64.decode(jwtProperties.getSecretKey())
                        )
                )
                .build()
                .parseSignedClaims(token) // 토큰 분석해
                .getPayload().get("id", Long.class);

        assertThat(tokenId).isEqualTo(testUser.getId());
    }

    @DisplayName("토큰 만료 검사하기 -> 만료되면 유효성 검증 실패")
    @Test
    void validToken_expiresAt() {
        // given - 5일전 생성
        String token = JwtFactory.builder()
                .expiresAt(
                        new Date(new Date().getTime() - Duration.ofDays(5).toMillis())
                )
                .build()
                .createToken(jwtProperties);

        // when
        boolean _result = tokenProvider.validToken(token);

        // then
        assertThat(_result).isFalse();
    }

    @DisplayName("유효한 정상 토큰 생성 및 유효성 검사 true")
    @Test
    void validToken_true() {
        // given - 토큰 생성
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);

        // when - 결과 확인
        boolean result = tokenProvider.validToken(token);

        // then - 결과 출력
        assertThat(result).isTrue();
    }

    @DisplayName("토큰에서 인증 정보를 가지고 옵니다.")
    @Test
    void getAuthentication() {
        // given
        String email = "spring@dgsw.hs.kr";
        User dbUser = userRepository.save(
                User.builder()
                        .email(email)
                        .password("spring")
                        .role(UserRole.USER)
                        .build()
        );

        // when
        String token = JwtFactory.builder()
                .subject(email)
                .claims(Map.of("id", dbUser.getId()))
                .build()
                .createToken(jwtProperties);

        // then
    }
}