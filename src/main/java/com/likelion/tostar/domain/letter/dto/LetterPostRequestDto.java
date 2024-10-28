package com.likelion.tostar.domain.letter.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LetterPostRequestDto {

    @Lob  // 긴 텍스트를 저장할 수 있도록 추가 ->  데이터베이스에서 TEXT으로 지정됨 (String->VARCHAR)
    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;
}
