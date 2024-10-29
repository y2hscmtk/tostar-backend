package com.likelion.tostar.domain.letter.dto;

import com.likelion.tostar.domain.letter.entity.SenderType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LetterSearchListDto {
    private Long letterId;
    private SenderType sender;
    private String content;
    private String createdAt;
}
