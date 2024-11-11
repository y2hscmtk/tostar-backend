package com.likelion.tostar.domain.comment.converter;

import com.likelion.tostar.domain.comment.dto.CommentResponseDTO;
import com.likelion.tostar.domain.comment.entity.Comment;
import com.likelion.tostar.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    public CommentResponseDTO toCommentResponseDTO(Comment comment) {
        User author = comment.getAuthor();
        return CommentResponseDTO.builder()
                .commentId(comment.getId())
                .petName(author.getPetName())
                .profileImage(author.getProfileImage())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
