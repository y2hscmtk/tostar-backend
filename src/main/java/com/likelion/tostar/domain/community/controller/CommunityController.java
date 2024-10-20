package com.likelion.tostar.domain.community.controller;

import com.likelion.tostar.domain.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/controller")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    /**
     * 커뮤니티 미리보기(랜덤)
     * 메인화면에서 사용; 3개의 랜덤 미리보기 반환
     */
    @GetMapping("preview/random")
    public ResponseEntity<?> getRandomPreviews() {
        return communityService.getRandomPreviews();
    }
}
