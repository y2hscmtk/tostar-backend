package com.likelion.tostar.global.openAi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * gpt 요청 반환에 필요한 dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTResponse {
    private List<Choice> choices;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice { // 이런 형태로 답장이 옴
        private int index;
        private Message message;

    }
}