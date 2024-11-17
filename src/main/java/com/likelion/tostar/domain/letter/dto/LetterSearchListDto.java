package com.likelion.tostar.domain.letter.dto;

import com.likelion.tostar.domain.letter.entity.SenderType;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LetterSearchListDto {
    private Long letterId;
    private String petName;
    private SenderType sender;
    private String content;
    private String createdAt;
}
