package com.likelion.tostar.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
public class CommentRequestDTO {
    @NotBlank(message = "댓글 내용이 비어있습니다.")
    private String content;
}