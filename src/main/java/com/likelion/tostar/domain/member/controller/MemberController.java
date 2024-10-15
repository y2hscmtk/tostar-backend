package com.likelion.tostar.domain.member.controller;

import com.likelion.tostar.domain.member.dto.JoinRequestDTO;
import com.likelion.tostar.domain.member.dto.LoginRequestDTO;
import com.likelion.tostar.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return memberService.login(loginRequestDTO);
    }

    /**
     * 회원 가입
     */
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody JoinRequestDTO joinRequestDTO) {
        return memberService.join(joinRequestDTO);
    }
}
