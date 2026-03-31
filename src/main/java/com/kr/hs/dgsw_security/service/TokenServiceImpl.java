package com.kr.hs.dgsw_security.service;

import com.kr.hs.dgsw_security.config.jwt.JwtProperties;
import com.kr.hs.dgsw_security.config.jwt.TokenProvider;
import com.kr.hs.dgsw_security.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final RefreshTokenService refreshTokenService;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final JwtProperties jwtProperties;

    @Override
    public String createNewAccessToken(String refreshToken) {
        if(tokenProvider.validToken(refreshToken) == false) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User byId = userService.findById(userId);
        Duration duration = Duration.ofMinutes(Long.parseLong(jwtProperties.getAccessExpirationMinutes()));

        return tokenProvider.generateToken(byId, duration);
    }
}
