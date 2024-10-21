package com.likelion.tostar.domain.community.converter;

import com.likelion.tostar.domain.community.dto.CommunityFormDTO;
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

    public Community toCommunity(CommunityFormDTO communityFormDTO) {
        return Community.builder()
                .profileImage(communityFormDTO.getProfileImage())
                .title(communityFormDTO.getTitle())
                .description(communityFormDTO.getDescription())
                .build();
    }
}
