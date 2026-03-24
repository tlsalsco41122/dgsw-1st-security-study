package com.kr.hs.dgsw_security.config.jwt;

import com.kr.hs.dgsw_security.domain.User;
import com.kr.hs.dgsw_security.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private SecretKey secretKey;

    @PostConstruct
    void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /*
     * Spring Bean lifecycle(도영왈)
     * 1. 스프링 컨테이너가 생성
     * 2. Bean 생성
     * 3. 의존성 주입
     * 4. @PostConstruct : 1번만 실행
     * 5. Bean 사용
     * 6. 객체 소멸 Callback( @PreDestroy )
     * 7. 스프링 종료
     */

    public String createToken(User user, Duration duration) {
        Date now = new Date();

    }

    private String makeToken(Date expire, User user) {
        Date now = new Date();
        return Jwts.builder()

    }
}
