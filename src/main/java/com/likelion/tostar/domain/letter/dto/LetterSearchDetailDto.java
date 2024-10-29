package com.likelion.tostar.domain.letter.dto;

import com.likelion.tostar.domain.letter.entity.SenderType;
import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LetterSearchDetailDto {
    private SenderType sender;
    @Lob  // 긴 텍스트를 저장할 수 있도록 추가 ->  데이터베이스에서 TEXT으로 지정됨 (String->VARCHAR)
    private String content;
}
