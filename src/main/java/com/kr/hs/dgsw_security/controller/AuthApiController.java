package com.kr.hs.dgsw_security.controller;

import com.kr.hs.dgsw_security.dto.SignupRequest;
import com.kr.hs.dgsw_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Controller + ResponseBody
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {
    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest req) {

    }
}
