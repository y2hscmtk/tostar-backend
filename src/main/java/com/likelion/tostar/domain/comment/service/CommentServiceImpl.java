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
    @Transactional
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
    @Transactional
    public CommentRequestDTO updateComment(Long articleId, Long commentId, CommentRequestDTO commentRequestDTO) {
//        // 댓글 조회
//        Optional<Comment> commentOptional = commentRepository.findById(commentId);
//        if (commentOptional.isEmpty()) {
//            throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
//        }
//
//        Comment comment = commentOptional.get();
//        comment.setContent(commentDTO.getContent());  // 수정된 내용으로 업데이트
//
//        // 수정된 댓글 저장
//        Comment updatedComment = commentRepository.save(comment);

        return CommentRequestDTO.builder().build();
    }

    /**
     * 댓글 삭제
     */
    @Override
    @Transactional
    public void deleteComment(Long articleId, Long commentId) {
        // 댓글 존재 여부 확인
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
        }

        // 댓글 삭제
        commentRepository.deleteById(commentId);
    }

    public Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException());
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
    }

}
