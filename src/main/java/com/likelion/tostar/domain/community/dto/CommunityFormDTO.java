package com.likelion.tostar.domain.community.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 커뮤니티 생성,수정 용
 */
@Data
@Builder
public class CommunityFormDTO {
    private String profileImage; // 커뮤니티 대표 이미지
    private String title; // 커뮤니티 명
    private String description; // 커뮤니티 소개
}
