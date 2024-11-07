package com.likelion.tostar.domain.articles.service;

import com.likelion.tostar.domain.articles.dto.ArticlePostRequestDto;
import com.likelion.tostar.domain.articles.dto.ArticlePostResponseDto;
import com.likelion.tostar.domain.articles.dto.ArticlePostResponseDto.ImageResponseDto;
import com.likelion.tostar.domain.articles.entity.Article;
import com.likelion.tostar.domain.articles.entity.ArticleImage;
import com.likelion.tostar.domain.articles.repository.ArticleRepository;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.domain.user.repository.UserRepository;
import com.likelion.tostar.global.exception.GeneralException;
import com.likelion.tostar.global.response.ApiResponse;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    /**
     * 게시글 작성 메서드
     */
    @Override
    public ResponseEntity<?> createArticle(Long userId, ArticlePostRequestDto articlePostRequestDto, List<MultipartFile> images) {
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

        // Article 엔티티 빌드 및 사용자 정보 설정
        Article article = Article.builder()
                .user(user)
                .title(title)
                .content(content)
                .build();

        // 400 : 이미지가 5개 초과하는 경우
        if (images != null && images.size() > 5) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.onFailure(ErrorStatus._ARTICLE_TOO_MANY_IMAGES, null));
        }

        // S3 이미지 파일 업로드
        List<ArticleImage> articleImages = new ArrayList<>();
        if (images != null) {
            for (MultipartFile image : images) {
                try {
                    // S3에 이미지 업로드
                    String imageUrl = s3Service.uploadFile(image);

                    // ArticleImage 엔티티 생성
                    ArticleImage articleImage = ArticleImage.builder()
                            .url(imageUrl)
                            .build();
                    article.addImage(articleImage);

                    // articleImages 에 추가
                    articleImages.add(articleImage);

                } catch (IOException e) {
                    // 파일 업로드 실패 시 예외 처리
                    throw new GeneralException(ErrorStatus._S3_UPLOAD_FAIL);
                }
            }
        }

        // Article 엔티티 저장
        articleRepository.save(article);

        // imageResponseDtos 생성
        List<ImageResponseDto> imageResponseDtos = new ArrayList<>();
        for (ArticleImage articleImage : articleImages) {
            ImageResponseDto imageResponseDto = ImageResponseDto.builder()
                    .imageId(articleImage.getId())
                    .url(articleImage.getUrl())
                    .build();
            imageResponseDtos.add(imageResponseDto);
        }

        // ArticlePostResponseDto 생성
        ArticlePostResponseDto responseDto = ArticlePostResponseDto.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .createdAt(article.localDateTimeToString())
                .updatedAt(article.localDateTimeToString())
                .images(imageResponseDtos)
                .build();

        // 201 : 게시글 작성 성공
        return ResponseEntity.status(201).body(ApiResponse.onSuccess(responseDto));
    }
}
