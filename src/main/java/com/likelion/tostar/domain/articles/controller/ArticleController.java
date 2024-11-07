package com.likelion.tostar.domain.articles.controller;

import com.likelion.tostar.domain.articles.dto.ArticlePostRequestDto;
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
     * 게시글 작성
     */
    @PostMapping
    public ResponseEntity<?> createArticle(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @ModelAttribute ArticlePostRequestDto articlePostRequestDto,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        return articleService.createArticle(customUserDetails.getId(), articlePostRequestDto, images);
    }
}
