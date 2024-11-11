package com.likelion.tostar.domain.comment.controller;

import com.likelion.tostar.domain.comment.dto.CommentRequestDTO;
import com.likelion.tostar.domain.comment.service.CommentService;
import com.likelion.tostar.global.jwt.dto.CustomUserDetails;
import com.likelion.tostar.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 특정 게시글에 대한 댓글들을 최신순으로 조회
     */
    @GetMapping
    public ResponseEntity<List<CommentRequestDTO>> getComments(@PathVariable Long articleId) {
        List<CommentRequestDTO> comments = commentService.getCommentsByArticleId(articleId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 작성
     */
    @PostMapping
    public ResponseEntity<?> createComment(
            @PathVariable Long articleId,
            @RequestBody CommentRequestDTO commentRequestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return commentService.createComment(articleId, commentRequestDTO, customUserDetails.getEmail());
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDTO commentRequestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetail) {
        return commentService.updateComment(commentId, commentRequestDTO, customUserDetail.getEmail());
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        commentService.deleteComment(articleId, commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}