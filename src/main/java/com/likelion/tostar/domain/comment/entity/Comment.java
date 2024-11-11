package com.likelion.tostar.domain.comment.entity;

import com.likelion.tostar.global.entity.BaseEntity;
import com.likelion.tostar.domain.article.Article;
import com.likelion.tostar.domain.user.entity.User;

import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Commnet")
public class Comment extends BaseEntity {
    // 댓글 ID
    @Id // PK 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 ID (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId", nullable = false)
    private Article article;

    // 작성자 ID (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Long userId;

    // 댓글 내용
    @NotBlank(message = "댓글 내용이 비어있습니다.")
    @Column(nullable = false)
    private String content;
}