package com.likelion.tostar.domain.comment.service;

import com.likelion.tostar.domain.comment.dto.CommentRequestDTO;

import java.util.List;
import org.springframework.http.ResponseEntity;
/**
 * GET 이외의 요청은 여기에 작성
 */
public interface CommentCommandService {
    // 댓글 작성
    ResponseEntity<?> createComment(Long articleId, CommentRequestDTO commentRequestDTO, String email);

    // 댓글 수정
    ResponseEntity<?> updateComment(Long commentId,CommentRequestDTO commentRequestDTO, String email);

    // 댓글 삭제
    ResponseEntity<?> deleteComment(Long commentId, String email);
}
