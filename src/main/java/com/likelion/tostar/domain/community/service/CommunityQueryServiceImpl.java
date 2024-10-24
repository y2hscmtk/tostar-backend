package com.likelion.tostar.domain.community.service;

import com.likelion.tostar.domain.community.converter.CommunityConverter;
import com.likelion.tostar.domain.community.dto.CommunityPreviewResponseDTO;
import com.likelion.tostar.domain.community.dto.CommunityProfileResponseDTO;
import com.likelion.tostar.domain.community.entity.Community;
import com.likelion.tostar.domain.community.repository.CommunityRepository;
import com.likelion.tostar.domain.community.repository.MemberRepository;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.domain.user.repository.UserRepository;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.exception.GeneralException;
import com.likelion.tostar.global.response.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityQueryServiceImpl implements CommunityQueryService{
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
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

    @Override
    public ResponseEntity<?> getAllPreviews(Pageable pageable) {
        Pageable defaultPageable = getDefaultPageable(pageable); // 최신 생성순 조회

        Page<Community> allCommunities = communityRepository.findAll(defaultPageable);

        // 반환 DTO 작성
        ArrayList<CommunityPreviewResponseDTO> resultDTOList = new ArrayList<>();
        for (Community community : allCommunities.getContent()) {
            resultDTOList.add(
                    communityConverter.toCommunityPreviewResponseDTO(community));
        }

        return ResponseEntity.ok(ApiResponse.onSuccess(resultDTOList));
    }

    @Override
    public ResponseEntity<?> getMyCommunities(Pageable pageable, String email) {
        // 1. 회원 정보 조회
        User user = findUserByEmail(email);

        // 2. 정렬 기준 설정
        Pageable defaultPageable = getDefaultPageable(pageable);

        // 2. 연관된 회원 정보 조회
        Page<Community> myCommunities = memberRepository.findMyCommunities(user, defaultPageable);

        // 4. 반환 DTO 작성
        List<CommunityPreviewResponseDTO> resultDTOList = new ArrayList<>();
        for (Community community : myCommunities.getContent()) {
            resultDTOList.add(
                    communityConverter.toCommunityPreviewResponseDTO(community));
        }

        return ResponseEntity.ok(ApiResponse.onSuccess(resultDTOList));
    }

    @Override
    public ResponseEntity<?> getCommunityPreview(Long communityId) {
        Community community = findCommunityById(communityId);
        CommunityProfileResponseDTO resultDTO = communityConverter.toCommunityProfileResponseDTO(
                community);
        return ResponseEntity.ok(ApiResponse.onSuccess(resultDTO));
    }

    /**
     * 정렬 기준 : 최신 작성(생성) 순
     */
    public Pageable getDefaultPageable(Pageable pageable) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
    }

    private User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
    }

    private Community findCommunityById(Long communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._COMMUNITY_NOT_FOUND));
    }
}
