package com.likelion.tostar.domain.community.service;

import com.likelion.tostar.domain.community.converter.CommunityConverter;
import com.likelion.tostar.domain.community.dto.CommunityPreviewResponseDTO;
import com.likelion.tostar.domain.community.dto.CommunityProfileResponseDTO;
import com.likelion.tostar.domain.community.entity.Community;
import com.likelion.tostar.domain.community.entity.mapping.Member;
import com.likelion.tostar.domain.community.repository.CommunityRepository;
import com.likelion.tostar.domain.community.repository.MemberRepository;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.domain.user.repository.UserRepository;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.exception.GeneralException;
import com.likelion.tostar.global.response.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
     * 커뮤니티 회원 추가
     */
    @Override
    public boolean addMemberToCommunity(Community community, User user) {
        // 커뮤니티 회원 여부 파악
        Optional<Member> membership = memberRepository.findMembership(community, user);
        if (membership.isPresent()) { // 이미 회원이라면 - 입장이 아님
            return false;
        }
        // 회원이 아니라면 커뮤니티에 회원가입
        community.addMember(user);
        return true;
    }

    /**
     * 커뮤니티 회원 제거
     */
    @Override
    public void removeMemberFromCommunity(Community community, User user) {
        Member member = findMemberShip(community, user);
        // 회원 제거
        community.deleteMember(member);
        memberRepository.delete(member);
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

    private Member findMemberShip(Community community, User user) {
        return memberRepository.findMembership(community, user)
                .orElseThrow(() -> new GeneralException(ErrorStatus._MEMBER_NOT_FOUND));
    }
}
