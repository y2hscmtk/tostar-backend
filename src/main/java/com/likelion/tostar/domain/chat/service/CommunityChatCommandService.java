package com.likelion.tostar.domain.chat.service;

import com.likelion.tostar.domain.chat.dto.CommunityChatRequestDTO;

/**
 * GET을 제외한 요청 작성
 */
public interface CommunityChatCommandService {
    void sendMessage(CommunityChatRequestDTO messageDTO, String email);
    void enterChatRoom(Long chatRoomId, String email);
}
