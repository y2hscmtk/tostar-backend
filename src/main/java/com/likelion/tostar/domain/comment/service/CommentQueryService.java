package com.likelion.tostar.domain.comment.service;

import com.likelion.tostar.domain.comment.dto.CommentRequestDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * GET 요청은 여기에 작성
 */
public interface CommentQueryService {
    // 댓글 조회
    ResponseEntity<?> getCommentsByArticleId(Long articleId);
}
