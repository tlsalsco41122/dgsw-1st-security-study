package com.kr.hs.dgsw_security.controller;

import com.kr.hs.dgsw_security.domain.User;
import com.kr.hs.dgsw_security.dto.AuthRequest;
import com.kr.hs.dgsw_security.dto.AuthResponse;
import com.kr.hs.dgsw_security.dto.SignupRequest;
import com.kr.hs.dgsw_security.dto.UserResponse;
import com.kr.hs.dgsw_security.service.AuthService;
import com.kr.hs.dgsw_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Controller + ResponseBody
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {
    private final UserService userService;
    private final AuthService authService;
    /**
     * 1. @RequestBody : body --> req --> json
     * 2. @ModelAttribute : form 자바객체로 한번받음
     * 3. @RequestParam : get(?a=3), form
     * 4. @PathVariable : /user/1/aaa/2, url 경로에서 변수값 받음
     *
     * 5. @RequestHeader : header값 추출할 때
     * 6. @CookieValue : cookie값 추출할 때
     * 7. @RequestPart : 파일 + 데이터, multipart/form-data에서 파일 추출할 때
     * 8. @HttpServletRequest : 옛날 거
     */

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest req) {
        User user = userService.signup(req);
        UserResponse userResponse = new UserResponse(user.getId(), user.getEmail(), user.getRole());

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        AuthResponse authRes = authService.login(req);
        return ResponseEntity.ok(authRes);
    }
}
