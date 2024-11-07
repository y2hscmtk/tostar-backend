package com.likelion.tostar.domain.articles.service;

import com.likelion.tostar.domain.articles.dto.ArticlePostRequestDto;
import org.springframework.http.ResponseEntity;

public interface ArticleService {
    // 게시글 생성
    ResponseEntity<?> createArticle(Long id, ArticlePostRequestDto articlePostRequestDto);
}
