package com.kr.hs.dgsw_security.security;

import com.kr.hs.dgsw_security.domain.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final User user;
    private Collection<? extends GrantedAuthority> authorities;
    private int loginFailCount = 0;

    public CustomUserDetails(User user) {
        this.user = user;
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole().getKey()));
    }

    /**
     * 사용자가 가지고 있는 권한 리스트를 반환
     * 생성자에서 미리 만들어둔 권한을 리턴
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 비밀번호 리턴, + 암호화
     */
    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    /**
     * 사용자 이름 리턴
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * 계정이 만료되었는가?
     * true: 만료되지 않음, false: 만료됨
     * 구독 서비스
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겨있는가?
     * true: 잠겨있지 않음, false: 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        // return loginFailCount < 5;
        return true;
    }

    /**
     * 비밀번호가 만료되었는가?
     * true: 만료되지 않음, false: 만료됨
      */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용가능한가?
     * true: 사용가능, false: 불가능
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
