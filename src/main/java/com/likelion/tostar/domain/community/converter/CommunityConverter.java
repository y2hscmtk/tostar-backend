package com.likelion.tostar.domain.community.converter;

import com.likelion.tostar.domain.community.dto.CommunityPreviewResponseDTO;
import com.likelion.tostar.domain.community.entity.Community;
import org.springframework.stereotype.Component;

@Component
public class CommunityConverter {

    public CommunityPreviewResponseDTO toCommunityPreviewResponseDTO(Community community) {
        return CommunityPreviewResponseDTO.builder()
                .profileImage(community.getProfileImage())
                .title(community.getTitle())
                .description(community.getDescription())
                .build();
    }
}
