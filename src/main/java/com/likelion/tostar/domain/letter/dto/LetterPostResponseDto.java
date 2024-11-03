package com.likelion.tostar.domain.letter.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LetterPostResponseDto {
    private Long receivedLetter;
}
