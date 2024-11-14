package com.likelion.tostar.domain.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 친구 목록 조회 시 사용
 */
@Data
@Builder
public class SearchFriendListDto {
    private Long id;
    private String petName;
    private String profileImage;
    private String category;
    private String birthday;
    private String starDay;
}
