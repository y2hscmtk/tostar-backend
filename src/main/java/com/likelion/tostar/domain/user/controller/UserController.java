package com.likelion.tostar.domain.user.controller;

import com.likelion.tostar.domain.user.dto.AddFriendDto;
import com.likelion.tostar.domain.user.dto.UserInfoDTO;
import com.likelion.tostar.domain.user.dto.UserJoinDTO;
import com.likelion.tostar.domain.user.dto.LoginRequestDTO;
import com.likelion.tostar.domain.user.service.UserService;
import com.likelion.tostar.global.jwt.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 회원 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.login(loginRequestDTO);
    }

    /**
     * 회원 가입
     */
    @PostMapping("/join")
    public ResponseEntity<?> join(
            @RequestParam("image") MultipartFile image,
            @Valid @ModelAttribute UserJoinDTO userJoinDTO) throws IOException {
        return userService.join(image, userJoinDTO);
    }


    /**
     * 회원 개인 정보 열람(개인 정보 수정 전)
     */
    @GetMapping("/info")
    public ResponseEntity<?> info(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.info(customUserDetails.getEmail());
    }

    /**
     * 회원 개인 정보 수정
     */
    @PutMapping("/edit")
    public ResponseEntity<?> edit(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("image") MultipartFile image,
            @Valid @ModelAttribute UserInfoDTO userInfoDTO) throws IOException {
        return userService.edit(image, userInfoDTO, customUserDetails.getEmail());
    }

    /**
     * 회원 검색
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("name") String petName,
            @RequestParam(value = "page", defaultValue = "0") int page,  // 디폴트 값 : 0
            @RequestParam(value = "size", defaultValue = "5") int size) { // 디폴트 값 : 5
        return userService.searchUser(customUserDetails.getId(), petName, page, size);
    }

    /**
     * 친구 추가
     */
    @PostMapping("/friend")
    public ResponseEntity<?> addFriend(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody AddFriendDto addFriendDto) {
        return userService.addFriend(customUserDetails.getEmail(), addFriendDto);
    }

    /**
     * 친구 전체 조회
     */
    @GetMapping("/friend")
    public ResponseEntity<?> searchFriend(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.searchFriend(customUserDetails.getId());
    }
}
