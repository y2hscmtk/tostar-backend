package com.likelion.tostar.domain.comment.dto;

import lombok.*;

@Data
@Builder
public class CommentRequestDTO {
    private String content;
}