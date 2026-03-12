package com.kr.hs.dgsw_security.dto;

import com.kr.hs.dgsw_security.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private UserRole role;
}
