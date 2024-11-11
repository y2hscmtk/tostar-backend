package com.likelion.tostar.domain.comment.controller;

import com.likelion.tostar.domain.comment.dto.CommentRequestDTO;
import com.likelion.tostar.domain.comment.service.CommentCommandService;
import com.likelion.tostar.domain.comment.service.CommentQueryService;
import com.likelion.tostar.global.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/")
public class CommentController {
    private final CommentCommandService commentService;
    private final CommentQueryService commentQueryService;

    /**
     * 특정 게시글에 대한 댓글들을 최신순으로 조회
     */
    @GetMapping("/{articleId}")
    public ResponseEntity<?> getComments(@PathVariable Long articleId) {
        return commentQueryService.getCommentsByArticleId(articleId);
    }

    /**
     * 댓글 작성
     */
    @PostMapping("{commentId}")
    public ResponseEntity<?> createComment(
            @PathVariable("commentId") Long articleId,
            @RequestBody CommentRequestDTO commentRequestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return commentService.createComment(articleId, commentRequestDTO, customUserDetails.getEmail());
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentRequestDTO commentRequestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetail) {
        return commentService.updateComment(commentId, commentRequestDTO, customUserDetail.getEmail());
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal CustomUserDetails customUserDetail) {
        return commentService.deleteComment(commentId, customUserDetail.getEmail());
    }
}