package com.likelion.tostar.domain.chat.dto;

import com.likelion.tostar.domain.chat.entity.enums.MessageType;
import lombok.Builder;
import lombok.Data;

/**
 * 새로운 메시지 반환용
 */
@Data
@Builder
public class CommunityChatResponseDTO {
    private String email; // 송신자 정보
    private MessageType messageType;
    private String profileImage;
    private String content;
}
