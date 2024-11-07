package com.likelion.tostar.domain.articles.entity;

import com.likelion.tostar.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ArticleImage")
public class ArticleImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    // ====== 편의 메서드 ========
    public void addArticle(Article article) {
        this.article = article;
    }
}