package com.kr.hs.dgsw_security.service;

import com.kr.hs.dgsw_security.domain.RefreshToken;
import com.kr.hs.dgsw_security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected Token"));
    }

    @Transactional //롤백, 모두 성공, 모두 실패
    @Override
    public RefreshToken saveOrUpdate(Long userId, String refreshToken) {
        return refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(refreshToken))
                .map(refreshTokenRepository::save)
                .orElseGet(() -> refreshTokenRepository.save(
                        new RefreshToken(userId, refreshToken)
                ));
    }

}
