package com.kr.hs.dgsw_security.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("USER_ADMIN");

    private final String key;
}
