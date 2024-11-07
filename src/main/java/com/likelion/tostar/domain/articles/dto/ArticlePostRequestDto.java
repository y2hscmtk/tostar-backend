package com.likelion.tostar.domain.articles.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ArticlePostRequestDto {
    private String title;
    private String content;
}
