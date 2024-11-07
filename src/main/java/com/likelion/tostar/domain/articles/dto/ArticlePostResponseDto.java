package com.likelion.tostar.domain.articles.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class ArticlePostResponseDto {
    private Long articleId;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
    private List<ImageResponseDto> images;

    @Data
    @Builder
    public static class ImageResponseDto {
        private Long imageId;
        private String url;
    }
}