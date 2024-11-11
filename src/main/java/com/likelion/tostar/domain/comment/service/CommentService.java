package com.likelion.tostar.domain.comment.service;

import com.likelion.tostar.domain.comment.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    // 댓글 조회
    List<CommentDTO> getCommentsByArticleId(Long articleId);

    // 댓글 작성
    CommentDTO createComment(Long articleId, CommentDTO commentDTO);

    // 댓글 수정
    CommentDTO updateComment(Long articleId, Long commentId, CommentDTO commentDTO);

    // 댓글 삭제
    void deleteComment(Long articleId, Long commentId);
}
