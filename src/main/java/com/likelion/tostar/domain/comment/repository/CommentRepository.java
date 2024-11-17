package com.likelion.tostar.domain.comment.repository;

import com.likelion.tostar.domain.articles.entity.Article;
import com.likelion.tostar.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.article = :article ORDER BY c.createdAt ASC")
    List<Comment> findCommentByArticle(@Param("article") Article article);
}