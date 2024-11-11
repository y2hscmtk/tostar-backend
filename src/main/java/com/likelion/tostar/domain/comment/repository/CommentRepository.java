package com.likelion.tostar.domain.comment.repository;

import com.likelion.tostar.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 댓글을 최신순으로 조회
    List<Comment> findByArticleIdOrderByCreatedAtDesc(Long articleId);
}