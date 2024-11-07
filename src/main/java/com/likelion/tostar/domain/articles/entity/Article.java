package com.likelion.tostar.domain.articles.entity;

import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Article")
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    // ArticleImage의 생명주기를 Article에 종속
    @Builder.Default
    private List<ArticleImage> images = new ArrayList<>();


    // ======= 편의 메서드 =========
    public void addImage(ArticleImage image) {
        images.add(image);
        image.addArticle(this);
    }
}
