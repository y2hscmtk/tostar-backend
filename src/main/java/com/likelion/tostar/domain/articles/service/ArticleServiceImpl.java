package com.likelion.tostar.domain.articles.service;

import com.likelion.tostar.domain.articles.dto.ArticlePostRequestDto;
import com.likelion.tostar.domain.articles.entity.Article;
import com.likelion.tostar.domain.articles.repository.ArticleRepository;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.domain.user.repository.UserRepository;
import com.likelion.tostar.global.exception.GeneralException;
import com.likelion.tostar.global.response.ApiResponse;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 작성
     */
    @Override
    public ResponseEntity<?> createArticle(Long userId, ArticlePostRequestDto articlePostRequestDto) {
        // 404 : 해당 회원이 실제로 존재하는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        String title = articlePostRequestDto.getTitle();
        String content = articlePostRequestDto.getContent();

        // 400 : 제목이 비어있는 경우
        if (title == null || title.isBlank()) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.onFailure(ErrorStatus._ARTICLE_TITLE_MISSING, null));
        }

        // 400 : 내용이 비어있는 경우
        if (content == null || content.isBlank()) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.onFailure(ErrorStatus._ARTICLE_CONTENT_MISSING, null));
        }

        // 게시글 저장
        Article article = Article.builder()
                .user(user)
                .title(title)
                .content(content)
                .build();
        articleRepository.save(article);

        // 결과 데이터 생성
        ArticlePostResponseDto result = ArticlePostResponseDto.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();

        // 201 : 작성 성공
        return ResponseEntity.status(201).body(ApiResponse.onSuccess(result));
    }
}
