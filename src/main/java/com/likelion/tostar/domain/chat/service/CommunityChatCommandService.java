package com.likelion.tostar.domain.chat.service;

import com.likelion.tostar.domain.chat.dto.ChatMessageRequestDTO;

/**
 * GET을 제외한 요청 작성
 */
public interface CommunityChatCommandService {
    void sendMessage(ChatMessageRequestDTO messageDTO, String email);
    void enterChatRoom(Long chatRoomId, String email);
}
