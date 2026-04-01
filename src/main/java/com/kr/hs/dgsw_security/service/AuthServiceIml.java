package com.kr.hs.dgsw_security.service;

import com.kr.hs.dgsw_security.config.jwt.JwtProperties;
import com.kr.hs.dgsw_security.config.jwt.TokenProvider;
import com.kr.hs.dgsw_security.domain.RefreshToken;
import com.kr.hs.dgsw_security.domain.User;
import com.kr.hs.dgsw_security.dto.AuthRequest;
import com.kr.hs.dgsw_security.dto.AuthResponse;
import com.kr.hs.dgsw_security.repository.UserRepository;
import com.kr.hs.dgsw_security.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthServiceIml implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;

    @Override
    public AuthResponse login(AuthRequest _req) {
        /* 이거 내일 시험 문제로 나옴, UsernamePasswordAuthenticationToken */
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        _req.getEmail(), _req.getPassword()
                )
        );
        /* User 엔티티를 시큐리티가 알려면 CustomUserDetails를 상속받아야한다(?) */
        User user = ((CustomUserDetails) authenticate.getPrincipal()).getUser();
        /* 처음부터 jwtProperties를 걍 Long으로 하기, 만약 처음부터 그렇게 설정했으면 파싱하면 안됨 */
        Duration accessTokenExpire = Duration.ofMinutes(Long.parseLong(jwtProperties.getAccessExpirationMinutes()));
        Duration refreshTokenExpire = Duration.ofDays(Long.parseLong(jwtProperties.getRefreshExpirationDays()));

        String accessToken = tokenProvider.generateToken(user, accessTokenExpire);
        String refreshToken = tokenProvider.generateToken(user, refreshTokenExpire);

        refreshTokenService.saveOrUpdate(user.getId(), refreshToken);

        return new AuthResponse(accessToken, refreshToken);
    }

}
