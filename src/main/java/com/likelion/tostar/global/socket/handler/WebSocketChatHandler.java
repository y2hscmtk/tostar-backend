package com.likelion.tostar.global.socket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.tostar.global.socket.dto.ChatMessageDTO;
import com.likelion.tostar.global.socket.dto.ChatMessageDTO.MessageType;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;

    // 현재 서버 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // 특정 채팅방에 소속된 세션 정보
    // key : chatRoomId ; Long
    // value : {session1, session2, ...} ; Set<WebSocketSession>
    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} 연결됨", session.getId());
        sessions.add(session); // 클라이언트 세션 정보 저장
    }

    // 소켓 통신 발생시, handleTextMessage 가 호출됨
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 소켓을 통해 전송된 message -> chatMessageDTO 로 변환
        String payload = message.getPayload();
        log.info("payload {}", payload);
        ChatMessageDTO chatMessageDto = mapper.readValue(payload, ChatMessageDTO.class);

        Long chatRoomId = chatMessageDto.getChatRoomId();
        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            chatRoomSessionMap.put(chatRoomId, new HashSet<>());
        }
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        boolean isSuccess = true;
        // 입장의 경우
        if (chatMessageDto.getMessageType().equals(MessageType.CONNECT)) {
            chatRoomSession.add(session);
            log.info("{} 연결/입장", session.getId());
        } // 퇴장의 경우
        else if (chatMessageDto.getMessageType().equals(MessageType.EXIT)) {
            chatRoomSession.remove(session);
            sessions.remove(session);
            log.info("{} 퇴장", session.getId());
        } // 소켓 연결 해제의 경우
        else if (chatMessageDto.getMessageType().equals(MessageType.DISCONNECT)) {
            chatRoomSession.remove(session);
            sessions.remove(session);
            log.info("{} 소켓 연결 해제", session.getId());
            isSuccess = false; // 연결 해제의 경우 별도의 메시지를 저장할 필요 없음
        }

        if (isSuccess) {

            sendMessageToChatRoom(chatMessageDto, chatRoomSession);
        }

    }

    // 소켓 종료 확인
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김 - Status: {}", session.getId(), status);
        sessions.remove(session);
    }

    // ====== 채팅 관련 메소드 ======
    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }


    // 채팅방에 새로운 채팅 메시지 전달
    // 채팅방에 소속된 클라이언트 세션에게 모두 새로운 채팅 전송
    private void sendMessageToChatRoom(ChatMessageDTO chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, chatMessageDto));
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
