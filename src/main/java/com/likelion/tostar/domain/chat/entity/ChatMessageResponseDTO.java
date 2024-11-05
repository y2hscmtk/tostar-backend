package com.likelion.tostar.domain.chat.entity;

import com.likelion.tostar.domain.chat.entity.enums.MessageType;
import lombok.Builder;
import lombok.Data;

/**
 * 새로운 메시지 반환용
 */
@Data
@Builder
public class ChatMessageResponseDTO {
    private String email; // 송신자 정보
    private MessageType messageType;
    private String content;
}
