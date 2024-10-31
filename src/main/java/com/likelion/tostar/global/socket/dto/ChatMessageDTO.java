package com.likelion.tostar.global.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    public enum MessageType{
        // CON : 채팅방 입장 & 연결,
        // TALK : 채팅 전송
        // DISCON : 연결 끊기
        // EXIT : 채팅방 나가기
        CONNECT, TALK, EXIT, DISCONNECT
    }
    private MessageType messageType; // 메시지 타입
    private Long chatRoomId; // 방 번호
    private Long senderId; // 채팅을 보낸 사람
    private String message; // 메시지
}
