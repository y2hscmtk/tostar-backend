package com.likelion.tostar.domain.articles.controller;

import com.likelion.tostar.domain.articles.dto.ArticleCreateModifyRequestDto;
import com.likelion.tostar.domain.articles.service.ArticleService;
import com.likelion.tostar.global.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 추억 등록하기
     */
    @PostMapping
    public ResponseEntity<?> createArticle(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @ModelAttribute ArticleCreateModifyRequestDto articleCreateModifyRequestDto,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        return articleService.createArticle(customUserDetails.getId(), articleCreateModifyRequestDto, images);
    }

    /**
     * 추억 수정하기
     */
    @GetMapping("{articleId}")
    public ResponseEntity<?> modifyArticle(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("articleId") Long articleId,
            @ModelAttribute ArticleCreateModifyRequestDto articleCreateModifyRequestDto,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        return articleService.modifyArticle(articleId, customUserDetails.getId(), articleCreateModifyRequestDto, images);
    }

    /**
     * 추억 삭제하기
     */
    @DeleteMapping("{articleId}")
    public ResponseEntity<?> deleteArticle(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("articleId") Long articleId) {
        return articleService.deleteArticle(articleId, customUserDetails.getId());
    }

    /**
     * 나의 추억 조회 (최신순)
     */
    @GetMapping("/user")
    public ResponseEntity<?> getArticlesByUserId(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "page", defaultValue = "0")int page,
            @RequestParam(value = "size", defaultValue = "4")int size) {
        return articleService.getUserArticles(customUserDetails.getId(), page, size);
    }

    /**
     * 특정 친구의 추억 조회 (최신순)
     */
    @GetMapping("/user/{searchId}")
    public ResponseEntity<?> getArticlesByUserId(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long searchId,
            @RequestParam(value = "page", defaultValue = "0")int page,
            @RequestParam(value = "size", defaultValue = "4")int size) {
        return articleService.getFriendsArticlesByUserId(customUserDetails.getId(), searchId, page, size);
    }

    /**
     * 특정 사용자들을 제외한 추억 조회
     */
    @GetMapping("/others")
    public ResponseEntity<?> getArticlesWithoutFriends(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "page", defaultValue = "0")int page,
            @RequestParam(value = "size", defaultValue = "4")int size) {
        return articleService.getArticlesWithoutFriends(customUserDetails.getId(), page, size);

    }
}
