package com.kr.hs.dgsw_security.service;

import com.kr.hs.dgsw_security.domain.User;
import com.kr.hs.dgsw_security.domain.UserRole;
import com.kr.hs.dgsw_security.dto.SignupRequest;
import com.kr.hs.dgsw_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User signup(SignupRequest req) {
        return userRepository.save(User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(UserRole.USER)
                .build());
    }
}
