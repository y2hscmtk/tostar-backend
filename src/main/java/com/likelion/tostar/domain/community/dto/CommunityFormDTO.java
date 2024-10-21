package com.likelion.tostar.domain.community.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * 커뮤니티 생성,수정 용
 */
@Data
@Builder
public class CommunityFormDTO {
    @NotNull(message = "커뮤니티 명은 필수 항목입니다.")
    private String title; // 커뮤니티 명
    @NotNull(message = "커뮤니티 소개는 필수 항목입니다.")
    private String description; // 커뮤니티 소개
}
