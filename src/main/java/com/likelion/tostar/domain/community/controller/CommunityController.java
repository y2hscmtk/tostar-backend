package com.likelion.tostar.domain.community.controller;

import com.likelion.tostar.domain.community.service.CommunityCommandService;
import com.likelion.tostar.domain.community.service.CommunityQueryService;
import com.likelion.tostar.global.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/controller")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityQueryService communityQueryService;
    private final CommunityCommandService communityCommandService;

    /**
     * 커뮤니티 미리보기(랜덤)
     * 메인화면에서 사용; 3개의 랜덤 미리보기 반환
     */
    @GetMapping("preview/random")
    public ResponseEntity<?> getRandomPreviews() {
        return communityQueryService.getRandomPreviews();
    }

    /**
     * 커뮤니티 생성
     * 생성된 커뮤니티에 해당 회원 저장
     */
    @PostMapping()
    public ResponseEntity<?> createCommunity(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return communityCommandService.createCommunity(customUserDetails.getEmail());
    }
}
