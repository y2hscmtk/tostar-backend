package com.likelion.tostar.global.openAi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTResponse {
    private Message message; // 응답 메세지
}
