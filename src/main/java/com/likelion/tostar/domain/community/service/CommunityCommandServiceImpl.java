package com.likelion.tostar.domain.community.service;

import com.likelion.tostar.domain.community.converter.CommunityConverter;
import com.likelion.tostar.domain.community.dto.CommunityFormDTO;
import com.likelion.tostar.domain.community.entity.Community;
import com.likelion.tostar.domain.community.repository.CommunityRepository;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.domain.user.repository.UserRepository;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.exception.GeneralException;
import com.likelion.tostar.global.response.ApiResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityCommandServiceImpl implements CommunityCommandService {
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final CommunityConverter communityConverter;

    @Override
    public ResponseEntity<?> createCommunity(
            MultipartFile image, CommunityFormDTO communityFormDTO, String email) throws IOException {
        // 1. 회원 정보 조회
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        // 2. 커뮤니티 생성
        Community community = communityConverter.toCommunity(image,communityFormDTO);
        user.createCommunity(community);

        // 3. 커뮤니티 저장(+멤버 저장)
        communityRepository.save(community);

        return ResponseEntity.ok(ApiResponse.onSuccess("커뮤니티가 생성되었습니다."));
    }

    @Override
    public ResponseEntity<?> editCommunity(
            Long communityId, MultipartFile image, CommunityFormDTO communityFormDTO, String email) {
        return null;
    }
}
