package com.likelion.tostar.domain.comment.service;

import com.likelion.tostar.domain.comment.dto.CommentRequestDTO;

import java.util.List;

public interface CommentService {
    // 댓글 조회
    List<CommentRequestDTO> getCommentsByArticleId(Long articleId);

    // 댓글 작성
    CommentRequestDTO createComment(Long articleId, CommentRequestDTO commentRequestDTO);

    // 댓글 수정
    CommentRequestDTO updateComment(Long articleId, Long commentId, CommentRequestDTO commentRequestDTO);

    // 댓글 삭제
    void deleteComment(Long articleId, Long commentId);
}
