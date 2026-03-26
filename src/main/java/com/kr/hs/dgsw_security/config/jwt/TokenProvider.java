package com.kr.hs.dgsw_security.config.jwt;

import com.kr.hs.dgsw_security.domain.User;
import com.kr.hs.dgsw_security.repository.UserRepository;
import com.kr.hs.dgsw_security.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public String generateToken(User user, Duration expireAt) {
        Date now = new Date();
        return makeToken(
                new Date(now.getTime() + expireAt.toMillis()),
                user);
    }

    private String makeToken(Date expire, User user) {
        Date now = new Date();
        return Jwts.builder()
                .header().type("JWT") // A
                .and()
                .issuer(jwtProperties.getIssuer()) // 토큰 발급자
                .issuedAt(now) // 토큰 발급 시간
                .expiration(expire) // 토큰 만료 시간
                .subject(user.getEmail()) // 토큰 주체
                .claim("id", user.getId()) // 토큰에 담을 정보, 클레임 추가
                // B
                .signWith(secretKey) // 서명 -> A + B + secretKey
                .compact();
    }
    /*
     * 클레임
     * 비공개 - 내만 쓰는건가, 중복 신경 안써도됨.
     * 공개 - 내가 LIB 를 공개, 중복 허용 안됨.
     */

    // 토큰 유효성 검증 메소드
    public boolean validToken(String token) {
        try {
            Jwts.parser() // Jwts가 토큰 분석을 위해 파서를 준비
                    .verifyWith(secretKey) // 서명을 분석할 키를 전달
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    /*
     * 예외
     * 서명이 조작: SignatureException
     * 토큰 만료: ExpiredJwtException
     * 토큰 형식 오류: MalformedJwtException
     * 토큰 값 비어있음: IllegalArgumentException
     * 지원하지 않는 토큰: UnsupportedJwtException
     */

    // 클레임 정보(사용자 정보)를 가져오기 - payload
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // subject 뽑아내기, 등록된 클레임 정보
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    // 비공개(공개) 클레임
    public Long getUserId(String token) {
        return getClaims(token).get("id", Long.class);
    }

    /*Security 랑 연결하기
     * 역할 : Jwt 에서 정보를 뽑아서 Authentication 의 출입증으로 변환
     * Security 가 알아먹을 수 있는 객체로 변환해주는 역할
     * 아는거
     * - 사용자 정보 : UserDetails
     * - 인증 : Authentication
     */
    public Authentication getAuthentication(String token) {
        String email = getSubject(token); // 사용자 이메일

        // 토큰에서 추출한 이메일이 우리 디비에서 아직도 사용 가능한 이메일인지 재확인
        User dbUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found" + email));

        CustomUserDetails customUserDetails = new CustomUserDetails(dbUser);

        return new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null, customUserDetails.getAuthorities());
    }
}
