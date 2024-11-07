package com.likelion.tostar.domain.articles.dto;

import lombok.Data;

@Data
public class ArticleCreateModifyRequestDto {
    private String title;
    private String content;
}
