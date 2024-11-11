package com.likelion.tostar.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponseDTO {
    private Long commentId; // 댓글 기본키
    private String petName;
    private String profileImage;
    private Boolean isMine; // 본인이 작성자인지 여부 반환
    private String content;
    private LocalDateTime updatedAt; // 최종 수정 시간
}