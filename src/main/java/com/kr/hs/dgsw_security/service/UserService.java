package com.kr.hs.dgsw_security.service;

import com.kr.hs.dgsw_security.domain.User;
import com.kr.hs.dgsw_security.dto.SignupRequest;

public interface UserService {
    User signup(SignupRequest req);
    User findById(Long id);

}
