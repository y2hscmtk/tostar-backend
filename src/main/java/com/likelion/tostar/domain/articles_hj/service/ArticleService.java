package com.likelion.tostar.domain.article.service;

import com.likelion.tostar.domain.article.dto.ArticleDTO;
import com.likelion.tostar.domain.article.entity.Article;
import com.likelion.tostar.domain.article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     * 특정 사용자의 게시글을 최신순으로 조회
     */
    public List<ArticleDTO> getArticlesByUserId(Long userId) {
        List<Article> articles = articleRepository.findAllByUserId(userId);
        return articles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 특정 사용자를 제외한 다른 사용자의 게시글을 최신순으로 조회 (페이징 포함)
     */
    public List<ArticleDTO> getOtherArticles(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Article> articles = articleRepository.findAllOthers(userId, pageRequest);
        return articles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Entity를 DTO로 변환하는 메서드
     */
    private ArticleDTO convertToDto(Article article) {
        return ArticleDTO.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .createdAt(article.getCreatedAt().toString())
                .updatedAt(article.getUpdatedAt().toString())
                .build();
    }
}
