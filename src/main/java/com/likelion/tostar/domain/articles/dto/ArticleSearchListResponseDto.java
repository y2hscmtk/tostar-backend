package com.likelion.tostar.domain.articles.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * 게시글 조회 관련 페이지 Dto
 */

@Data
@Builder
public class ArticleSearchListResponseDto {

    private Long articleId;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
    private AuthorDto author;
    private List<ImageDto> images; // 게시글 이미지들
    private boolean isOwner; // 게시글의 주인 여부

    @Builder
    @Data
    public static class AuthorDto { // 게시글 주인에 대한 정보
        private Long userId;
        private String profileImage;
        private String petName;
        private String category;
        private String birthDay;
        private String starDay;
    }

    @Builder
    @Data
    public static class ImageDto { // 게시글 이미지에 대한 정보
        private Long imageId;
        private String url;
    }

}
