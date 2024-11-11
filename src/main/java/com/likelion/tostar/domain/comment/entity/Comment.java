package com.likelion.tostar.domain.comment.entity;

import com.likelion.tostar.domain.comment.dto.CommentRequestDTO;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.global.entity.BaseEntity;
import com.likelion.tostar.domain.articles.entity.Article;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Commnet")
public class Comment extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author; // 댓글 작성자

    @Column(nullable = false)
    private String content; // 댓글 내용

    // == 편의 메소드 ==
    public static Comment toEntity(CommentRequestDTO dto, Article article, User author) {
        return Comment.builder()
                .article(article)
                .author(author)
                .content(dto.getContent())
                .build();
    }
}