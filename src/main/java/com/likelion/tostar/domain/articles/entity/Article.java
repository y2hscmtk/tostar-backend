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

    @OneToMany(mappedBy = "article",
            cascade = CascadeType.ALL, orphanRemoval = true)
            // ArticleImage의 생명주기를 Article에 종속
    @Builder.Default
    private List<ArticleImage> images = new ArrayList<>();


    // ========== 편의 메서드 ===========
    // 이미지 추가
    public void addImage(ArticleImage image) {
        images.add(image);
        image.addArticle(this);
    }

    // 추억 수정 (이미지 제외)
    public void updateArticle(String newTitle, String newContent) {
        this.title = newTitle;
        this.content = newContent;
    }

    // 이미지 수정
    public void updateImages(List<ArticleImage> newImages) {
        // 기존 이미지 모두 제거
        images.clear();

        // 새로운 이미지 추가
        for (ArticleImage newImage : newImages) {
            addImage(newImage);
        }
    }
}
