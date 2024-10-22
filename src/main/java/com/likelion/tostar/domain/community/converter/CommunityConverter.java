package com.likelion.tostar.domain.community.converter;

import com.likelion.tostar.domain.community.dto.CommunityFormDTO;
import com.likelion.tostar.domain.community.dto.CommunityPreviewResponseDTO;
import com.likelion.tostar.domain.community.entity.Community;
import com.likelion.tostar.global.s3.service.S3Service;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class CommunityConverter {
    private final S3Service s3Service;

    public CommunityPreviewResponseDTO toCommunityPreviewResponseDTO(Community community) {
        return CommunityPreviewResponseDTO.builder()
                .communityId(community.getId())
                .profileImage(community.getProfileImage())
                .title(community.getTitle())
                .description(community.getDescription())
                .build();
    }

    public Community toCommunity(MultipartFile image,CommunityFormDTO communityFormDTO) throws IOException {
        return Community.builder()
                .profileImage(s3Service.uploadFile(image))
                .title(communityFormDTO.getTitle())
                .description(communityFormDTO.getDescription())
                .build();
    }
}
