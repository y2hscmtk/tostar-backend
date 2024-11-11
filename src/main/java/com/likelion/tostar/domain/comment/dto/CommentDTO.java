package com.likelion.tostar.domain.comment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long commentId;
    private Long articleId;
    private Long userId;
    private String content;
}