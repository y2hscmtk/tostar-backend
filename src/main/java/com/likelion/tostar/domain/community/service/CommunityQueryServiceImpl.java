package com.likelion.tostar.domain.community.service;

import com.likelion.tostar.domain.community.converter.CommunityConverter;
import com.likelion.tostar.domain.community.dto.CommunityPreviewResponseDTO;
import com.likelion.tostar.domain.community.entity.Community;
import com.likelion.tostar.domain.community.repository.CommunityRepository;
import com.likelion.tostar.global.response.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{
    private final CommunityRepository communityRepository;
    private final CommunityConverter communityConverter;
    @Override
    public ResponseEntity<?> getRandomPreviews() {
        // 랜덤 커뮤니티 3개 추출
        List<Community> randomPreviews = communityRepository.getRandomPreviews();

        // 반환 DTO 작성
        ArrayList<CommunityPreviewResponseDTO> responseDTO = new ArrayList<>();

        for (Community community : randomPreviews) {
            responseDTO.add(
                    communityConverter.toCommunityPreviewResponseDTO(community));
        }

        return ResponseEntity.ok(ApiResponse.onSuccess(responseDTO));
    }
}
