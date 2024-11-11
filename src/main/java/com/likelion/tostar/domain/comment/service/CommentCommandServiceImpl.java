package com.likelion.tostar.domain.comment.service;

import com.likelion.tostar.domain.articles.entity.Article;
import com.likelion.tostar.domain.articles.repository.ArticleRepository;
import com.likelion.tostar.domain.comment.converter.CommentConverter;
import com.likelion.tostar.domain.comment.dto.CommentRequestDTO;
import com.likelion.tostar.domain.comment.entity.Comment;
import com.likelion.tostar.domain.comment.repository.CommentRepository;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.domain.user.repository.UserRepository;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.exception.GeneralException;
import com.likelion.tostar.global.response.ApiResponse;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentConverter commentConverter;


    /**
     * 댓글 작성
     */
    @Override
    @Transactional
    public ResponseEntity<?> createComment(Long articleId, CommentRequestDTO commentRequestDTO, String email) {
        // 1. 게시글 존재 여부 확인
        Article article = findArticleById(articleId);
        // 2. 회원 존재 여부 확인
        User user = findUserByEmail(email);
        // 3. 댓글 엔티티 생성 및 저장
        Comment comment = commentRepository.save(Comment.toEntity(commentRequestDTO, article, user));
        // 4. 반환 DTO 생성 및 반환
        return ResponseEntity.ok(ApiResponse.onSuccess(commentConverter.toCommentResponseDTO(comment,user)));
    }

    /**
     * 댓글 수정
     */
    @Override
    public ResponseEntity<?> updateComment(Long commentId, CommentRequestDTO commentRequestDTO, String email) {
        // 1. 댓글 존재 여부 확인
        Comment comment = findCommentById(commentId);
        // 2. 회원 존재 여부 확인
        User user = findUserByEmail(email);
        // 3. 댓글 작성자 - 수정 요청자 동일인 확인
        if (user != comment.getAuthor()) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }
        // 4. 댓글 수정
        comment.changeContent(commentRequestDTO.getContent());
        return ResponseEntity.ok(ApiResponse.onSuccess(commentConverter.toCommentResponseDTO(comment,user)));
    }

    /**
     * 댓글 삭제
     */
    @Override
    public ResponseEntity<?> deleteComment(Long commentId, String email) {
        // 1. 댓글 존재 여부 확인
        Comment comment = findCommentById(commentId);
        // 2. 사용자 존재 여부 확인
        User user = findUserByEmail(email);
        // 3. 댓글 작성자 - 삭제 요청자 동일 인물 확인
        if (user != comment.getAuthor()) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }
        // 4. 삭제
        commentRepository.delete(comment);
        return ResponseEntity.ok(ApiResponse.onSuccess("댓글이 삭제되었습니다."));
    }

    public Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._ARTICLE_NOT_FOUND));
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._COMMENT_NOT_FOUND));
    }

}
