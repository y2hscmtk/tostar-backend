package com.likelion.tostar.domain.community.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 특정 커뮤니티 미리보기시 사용
 */
@Data
@Builder
public class CommunityProfileResponseDTO {
    private Boolean isOwner; // 커뮤니티 주인 여부
    private String ownerPetName; // 커뮤니티 주인의 애완동물 이름
    private String ownerPetProfileImage; // 커뮤니티 주인의 애완동물 이미지 링크
    private Long communityId;
    private String communityProfileImage;
    private String communityName;
    private String communityDescription;
}
