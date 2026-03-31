package com.kr.hs.dgsw_security.service;

public interface TokenService {
    String createNewAccessToken(String refreshToken);
}
