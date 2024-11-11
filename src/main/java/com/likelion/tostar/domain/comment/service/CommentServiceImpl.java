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
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentConverter commentConverter;

    /**
     * 특정 게시글의 댓글 최신순 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<CommentRequestDTO> getCommentsByArticleId(Long articleId) {
        // 본인의 댓글인지 확인 가능한 속성도 함께 반환

        // 댓글 조회 (findByArticleIdOrderByCreatedAtDesc 메서드 사용)
        List<Comment> comments = commentRepository.findByArticleIdOrderByCreatedAtDesc(articleId);

        ArrayList<CommentRequestDTO> commentRequestDTOS = new ArrayList<>();
        // 댓글 엔티티를 DTO로 변환하여 반환
//        return comments.stream()
//                .map(comment -> new CommentDTO(comment.getId(),
//                        comment.getArticleId(), comment.getUserId(), comment.getContent()))
//                .collect(Collectors.toList());
        return commentRequestDTOS;
    }

    /**
     * 댓글 작성
     */
    @Override
    public ResponseEntity<?> createComment(Long articleId, CommentRequestDTO commentRequestDTO, String email) {
        // 1. 게시글 존재 여부 확인
        Article article = findArticleById(articleId);
        // 2. 회원 존재 여부 확인
        User user = findUserByEmail(email);
        // 3. 댓글 엔티티 생성
        Comment comment = Comment.toEntity(commentRequestDTO, article, user);
        // 4. 댓글 저장
        commentRepository.save(comment);
        // 5. 반환 DTO 생성 및 반환
        return ResponseEntity.ok(ApiResponse.onSuccess(commentConverter.toCommentResponseDTO(comment)));
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
        return ResponseEntity.ok(ApiResponse.onSuccess(commentConverter.toCommentResponseDTO(comment)));
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
                .orElseThrow(() -> new RuntimeException());
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException());
    }

}
