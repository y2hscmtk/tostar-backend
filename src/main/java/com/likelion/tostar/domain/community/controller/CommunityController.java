package com.likelion.tostar.domain.community.controller;

import com.likelion.tostar.domain.community.dto.CommunityFormDTO;
import com.likelion.tostar.domain.community.service.CommunityCommandService;
import com.likelion.tostar.domain.community.service.CommunityQueryService;
import com.likelion.tostar.global.jwt.dto.CustomUserDetails;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/community")
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
     * 모든 커뮤니티 미리보기(최신 작성순)
     */
    @GetMapping("preview/all")
    public ResponseEntity<?> getAllPreviews(Pageable pageable) {
        return communityQueryService.getAllPreviews(pageable);
    }

    /**
     * 내가 참여중인 커뮤니티 미리보기(최신 참여순)
     */
    @GetMapping("preview/my")
    public ResponseEntity<?> getMyCommunities(Pageable pageable) {
        return communityQueryService.getMyCommunities(pageable);
    }

    /**
     * 커뮤니티 생성
     * 생성된 커뮤니티에 해당 회원 저장
     */
    @PostMapping()
    public ResponseEntity<?> createCommunity(
            @RequestParam("image") MultipartFile image,
            @ModelAttribute CommunityFormDTO communityFormDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {

        return communityCommandService
                .createCommunity(image, communityFormDTO, customUserDetails.getEmail());
    }

    /**
     * 커뮤니티 수정
     */
    @PutMapping("/{communityId}")
    public ResponseEntity<?> editCommunity(
            @RequestParam("image") MultipartFile image,
            @PathVariable("communityId") Long communityId,
            @ModelAttribute CommunityFormDTO communityFormDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        return communityCommandService
                .editCommunity(communityId, image, communityFormDTO, customUserDetails.getEmail());
    }

    /**
     * 커뮤니티 삭제
     */
    @DeleteMapping("/{communityId}")
    public ResponseEntity<?> deleteCommunity(
            @PathVariable("communityId") Long communityId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return communityCommandService.deleteCommunity(communityId, customUserDetails.getEmail());
    }

    /**
     * 커뮤니티 가입
     */
    @PostMapping("/{communityId}/join")
    public ResponseEntity<?> joinCommunity(
            @PathVariable("communityId") Long communityId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return communityCommandService.joinCommunity(communityId, customUserDetails.getEmail());
    }

    /**
     * 커뮤니티 탈퇴
     */
    @PostMapping("/{communityId}/leave")
    public ResponseEntity<?> leaveCommunity(
            @PathVariable("communityId") Long communityId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return communityCommandService.leaveCommunity(communityId, customUserDetails.getEmail());
    }
}
