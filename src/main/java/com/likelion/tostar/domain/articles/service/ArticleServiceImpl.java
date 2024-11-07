package com.likelion.tostar.domain.articles.service;

import com.likelion.tostar.domain.articles.dto.ArticleCreateModifyRequestDto;
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
     * 추억 등록 메서드
     */
    @Override
    public ResponseEntity<?> createArticle(Long userId, ArticleCreateModifyRequestDto articleCreateModifyRequestDto, List<MultipartFile> images) {
        // 404 : 해당 회원이 실제로 존재하는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        String title = articleCreateModifyRequestDto.getTitle();
        String content = articleCreateModifyRequestDto.getContent();

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

        // Article 생성 (이미지 제외)
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

        // S3 이미지 업로드 및 ArticleImage 엔티티 생성
        List<ArticleImage> articleImages = uploadImages(images);
        article.updateImages(articleImages);

        // DB에 추억 저장
        articleRepository.save(article);

        // 응답 생성
        return createArticleResponse(article);
    }

    /**
     * 추억 수정 메서드
     */
    @Override
    public ResponseEntity<?> modifyArticle(Long articleId, Long userId, ArticleCreateModifyRequestDto articleCreateModifyRequestDto, List<MultipartFile> images) {
        // 404 : 해당 회원이 실제로 존재하는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        String title = articleCreateModifyRequestDto.getTitle();
        String content = articleCreateModifyRequestDto.getContent();

        // 404 : 존재하지 않는 추억
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._ARTICLE_NOT_FOUND));

        // 403 : 추억의 주인이 아님
        if (!article.getUser().getId().equals(userId)) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.onFailure(ErrorStatus._NOT_OWNER_OF_ARTICLE, null));
        }

        // 추억 수정 (이미지 제외한 정보들 수정)
        article.updateArticle(title, content);

        // 기존 이미지 삭제 및 새 이미지 업로드
        deleteExistingImages(article);
        List<ArticleImage> newImages = uploadImages(images);
        article.updateImages(newImages);

        // DB에 추억 저장
        articleRepository.save(article);

        // 응답 생성
        return createArticleResponse(article);
    }

    /**
     * 기존 이미지 삭제 메서드
     */
    private void deleteExistingImages(Article article) {
        for (ArticleImage existingImage : article.getImages()) {
            s3Service.deleteFileByURL(existingImage.getUrl()); // S3에서 이미지 삭제
        }
        article.getImages().clear();
    }

    /**
     * 이미지 업로드 및 ArticleImage 엔티티 생성 메서드
     */
    private List<ArticleImage> uploadImages(List<MultipartFile> images) {
        List<ArticleImage> articleImages = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                try {
                    String imageUrl = s3Service.uploadFile(image);
                    ArticleImage articleImage = ArticleImage.builder().url(imageUrl).build();
                    articleImages.add(articleImage);
                } catch (IOException e) {
                    throw new GeneralException(ErrorStatus._S3_UPLOAD_FAIL);
                }
            }
        }
        return articleImages;
    }

    /**
     * 게시글 응답 생성 메서드
     */
    private ResponseEntity<?> createArticleResponse(Article article) {
        List<ImageResponseDto> imageResponseDtos = new ArrayList<>();
        for (ArticleImage articleImage : article.getImages()) {
            ImageResponseDto imageResponseDto = ImageResponseDto.builder()
                    .imageId(articleImage.getId())
                    .url(articleImage.getUrl())
                    .build();
            imageResponseDtos.add(imageResponseDto);
        }

        ArticlePostResponseDto responseDto = ArticlePostResponseDto.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .createdAt(article.localDateTimeToString())
                .updatedAt(article.localDateTimeToString())
                .images(imageResponseDtos)
                .build();

        return ResponseEntity.status(200).body(ApiResponse.onSuccess(responseDto));
    }
}
