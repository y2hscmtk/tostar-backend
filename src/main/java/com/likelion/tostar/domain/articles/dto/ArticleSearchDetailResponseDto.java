package com.likelion.tostar.domain.articles.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ArticleSearchDetailResponseDto {

    private Long articleId;
    private String title;
    private String content;
    private List<ArticleSearchListResponseDto.ImageDto> images; // 게시글 이미지들
    private boolean isOwner; // 게시글의 주인 여부

    @Builder
    @Data
    public static class ImageDto { // 게시글 이미지에 대한 정보
        private Long imageId;
        private String url;
    }
}