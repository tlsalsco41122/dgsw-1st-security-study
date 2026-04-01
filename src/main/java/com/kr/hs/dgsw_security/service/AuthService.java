package com.kr.hs.dgsw_security.service;

import com.kr.hs.dgsw_security.dto.AuthRequest;
import com.kr.hs.dgsw_security.dto.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
}
