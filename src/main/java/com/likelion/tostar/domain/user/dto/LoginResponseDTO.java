package com.likelion.tostar.domain.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 로그인 성공시 일부 개인 정보 반환
 */
@Data
@Builder
public class LoginResponseDTO {
    private String petName; // 애완동물 이름
    private String userName; // 사용자 이름
    private String email; // 사용자 이메일
    private String accessToken; // JWT
}
