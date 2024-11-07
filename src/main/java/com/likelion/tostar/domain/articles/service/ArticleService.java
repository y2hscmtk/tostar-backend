package com.likelion.tostar.domain.articles.service;

import com.likelion.tostar.domain.articles.dto.ArticlePostRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {
    // 게시글 생성
    ResponseEntity<?> createArticle(Long userId, ArticlePostRequestDto articlePostRequestDto, List<MultipartFile> images);
}
