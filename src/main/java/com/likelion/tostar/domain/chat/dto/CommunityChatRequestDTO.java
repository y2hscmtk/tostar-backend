package com.likelion.tostar.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 새로운 메시지 전송용
 */
@Data
@Builder
public class CommunityChatRequestDTO {
    private Long chatRoomId;
    private String content;
}
