package com.likelion.tostar.global.openAi.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * gpt 요청 시 필요한 dto
 */
@Data
public class ChatGPTRequest {

    private String model;
    private List<Message> messages; // 대화 리스트

    public ChatGPTRequest(String model, String prompt) {
        this.model = model; // openAI 모델 설정 (현재 4o)
        this.messages = new ArrayList<>(); // 대화 리스트 초기화
        this.messages.add(new Message("user", prompt));
    }

    // 챗봇을 위한 기능 (프리캡스톤, 고모프 등..)
    public void addMessage(String role, String content) {
        this.messages.add(new Message(role, content)); // 대화 리스트 추가
    }
}
