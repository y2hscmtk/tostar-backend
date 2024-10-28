package com.likelion.tostar.global.openAi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 대화 시 주고받는 메세지
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String role; // 역할 (후에 챗봇 기능 구현할때를 대비해 만들어놓음)
    private String content; // 메세지 내용

}