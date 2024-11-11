package com.likelion.tostar.domain.article.controller;

import com.likelion.tostar.domain.article.dto.ArticleDTO;
import com.likelion.tostar.domain.article.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 나와 친구를 맺고 있는 회원들의 게시글 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ArticleDTO>> getArticlesByUserId(@PathVariable Long userId) {
        List<ArticleDTO> articles = articleService.getArticlesByUserId(userId);
        return ResponseEntity.ok(articles);
    }

    /**
     * 다른 회원들의 게시글 조회 (최신순 + 페이징)
     */
    @GetMapping("/others")
    public ResponseEntity<List<ArticleDTO>> getOtherArticles(
            @RequestParam Long userId,
            @RequestParam int page,
            @RequestParam int size) {
        List<ArticleDTO> articles = articleService.getOtherArticles(userId, page, size);
        return ResponseEntity.ok(articles);
    }
}