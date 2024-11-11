package com.likelion.tostar.domain.comment.converter;

import com.likelion.tostar.domain.comment.dto.CommentResponseDTO;
import com.likelion.tostar.domain.comment.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    public CommentResponseDTO toCommentResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .commentId(comment.getId())
                .authorId(comment.getAuthor().getId())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
