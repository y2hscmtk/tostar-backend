package com.likelion.tostar.domain.user.controller;

import com.likelion.tostar.domain.user.dto.UserInfoRequestDTO;
import com.likelion.tostar.domain.user.dto.LoginRequestDTO;
import com.likelion.tostar.domain.user.service.UserServiceImpl;
import com.likelion.tostar.global.jwt.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    /**
     * 회원 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return userServiceImpl.login(loginRequestDTO);
    }

    /**
     * 회원 가입
     */
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody UserInfoRequestDTO userInfoRequestDTO) {
        return userServiceImpl.join(userInfoRequestDTO);
    }


    /**
     * 회원 개인정보 열람(개인 정보 수정 전)
     */
    @GetMapping
    public ResponseEntity<?> info(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        customUserDetails.getEmail(); // 현재 로그인 한 사용자 이메일
        return null;
    }
}
