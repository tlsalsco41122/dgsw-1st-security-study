package com.kr.hs.dgsw_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my")
public class MyController {
    @GetMapping("/admin")
    public String getAdmin() {
        return "관리자 화면입니다.";
    }

    @GetMapping("/user")
    public String getUser() {
        return "유저 화면입니다.";
    }
}
