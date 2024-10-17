package com.likelion.tostar.domain.user.controller;

import com.likelion.tostar.domain.user.dto.UserJoinDTO;
import com.likelion.tostar.domain.user.dto.LoginRequestDTO;
import com.likelion.tostar.domain.user.service.UserService;
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
    private final UserService userService;

    /**
     * 회원 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.login(loginRequestDTO);
    }

    /**
     * 회원 가입
     */
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody UserJoinDTO userJoinDTO) {
        return userService.join(userJoinDTO);
    }


    /**
     * 회원 개인 정보 열람(개인 정보 수정 전)
     */
    @GetMapping("/info")
    public ResponseEntity<?> info(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.info(customUserDetails.getEmail());
    }
}
