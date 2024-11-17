package com.likelion.tostar.domain.community.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommunityPreviewResponseDTO {
    private Long communityId;
    private String profileImage;
    private String title;
    private String description;
}
