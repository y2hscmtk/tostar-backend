package com.likelion.tostar.domain.chat.controller;

import com.likelion.tostar.domain.chat.dto.CommunityChatRequestDTO;
import com.likelion.tostar.domain.chat.service.CommunityChatCommandService;
import com.likelion.tostar.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final CommunityChatCommandService communityChatCommandService;
    private final JwtUtil jwtUtil;

    // Client 요청 에시
    // stompClient.send(`/app/chat.sendMessage`, { 'Authorization': jwtToken }, JSON.stringify(chatMessage));
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload CommunityChatRequestDTO messageDTO, SimpMessageHeaderAccessor headerAccessor) {
        String email = jwtUtil.getEmailFromJWT(headerAccessor.getFirstNativeHeader("Authorization"));
        communityChatCommandService.sendMessage(messageDTO, email);
    }

    @MessageMapping("/chat.enter")
    public void enterChatRoom(@Payload CommunityChatRequestDTO messageDTO, SimpMessageHeaderAccessor headerAccessor) {
        String email = jwtUtil.getEmailFromJWT(headerAccessor.getFirstNativeHeader("Authorization"));
        communityChatCommandService.enterChatRoom(messageDTO.getChatRoomId(), email);
    }
}

