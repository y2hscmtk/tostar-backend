package com.likelion.tostar.domain.comment.service;

import com.likelion.tostar.domain.articles.entity.Article;
import com.likelion.tostar.domain.articles.repository.ArticleRepository;
import com.likelion.tostar.domain.comment.dto.CommentDTO;
import com.likelion.tostar.domain.comment.entity.Comment;
import com.likelion.tostar.domain.comment.repository.CommentRepository;
import com.likelion.tostar.domain.user.repository.UserRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    /**
     * 특정 게시글의 댓글 최신순 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByArticleId(Long articleId) {
        // 댓글 조회 (findByArticleIdOrderByCreatedAtDesc 메서드 사용)
        List<Comment> comments = commentRepository.findByArticleIdOrderByCreatedAtDesc(articleId);

        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        // 댓글 엔티티를 DTO로 변환하여 반환
//        return comments.stream()
//                .map(comment -> new CommentDTO(comment.getId(),
//                        comment.getArticleId(), comment.getUserId(), comment.getContent()))
//                .collect(Collectors.toList());
        return commentDTOS;
    }

    /**
     * 댓글 작성
     */
    @Override
    @Transactional
    public CommentDTO createComment(Long articleId, CommentDTO commentDTO) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException());
//
//        // 댓글 엔티티 생성 및 정보 설정
//        Comment.builder()
//                .article(article)
//
//
//        comment.setUserId(commentDTO.getUserId());
//        comment.setContent(commentDTO.getContent());
//
//        // 댓글 저장
//        Comment savedComment = commentRepository.save(comment);

        // 저장된 댓글 반환
        return new CommentDTO();
    }

    /**
     * 댓글 수정
     */
    @Override
    @Transactional
    public CommentDTO updateComment(Long articleId, Long commentId, CommentDTO commentDTO) {
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

        return new CommentDTO();
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
}
