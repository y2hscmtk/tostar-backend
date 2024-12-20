package com.likelion.tostar.domain.community.service;

import com.likelion.tostar.domain.chat.converter.ChatConverter;
import com.likelion.tostar.domain.chat.dto.CommunityChatResponseDTO;
import com.likelion.tostar.domain.chat.entity.CommunityChat;
import com.likelion.tostar.domain.chat.entity.enums.MessageType;
import com.likelion.tostar.domain.chat.repository.CommunityChatRepository;
import com.likelion.tostar.domain.community.converter.CommunityConverter;
import com.likelion.tostar.domain.community.dto.CommunityFormDTO;
import com.likelion.tostar.domain.community.entity.Community;
import com.likelion.tostar.domain.community.entity.mapping.Member;
import com.likelion.tostar.domain.community.repository.CommunityRepository;
import com.likelion.tostar.domain.community.repository.MemberRepository;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.domain.user.repository.UserRepository;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.exception.GeneralException;
import com.likelion.tostar.global.response.ApiResponse;
import com.likelion.tostar.global.s3.service.S3Service;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityCommandServiceImpl implements CommunityCommandService {
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final CommunityChatRepository communityChatRepository;
    private final MemberRepository memberRepository;
    private final CommunityConverter communityConverter;
    private final ChatConverter chatConverter;
    private final S3Service s3Service;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public ResponseEntity<?> createCommunity(
            MultipartFile image, CommunityFormDTO communityFormDTO, String email) throws IOException {
        // 1. 회원 정보 조회
        User user = findUserByEmail(email);

        // 2. 커뮤니티 이름 중복 검사
        communityRepository.findByTitle(communityFormDTO.getTitle()).ifPresent(community -> {
            throw new GeneralException(ErrorStatus._DUPLICATE_COMMUNITY_TITLE);
        });

        // 3. 커뮤니티 생성
        Community community = communityConverter.toCommunity(image,communityFormDTO);
        user.createCommunity(community);

        // 4. 커뮤니티 저장(+멤버 저장)
        communityRepository.save(community);

        return ResponseEntity.ok(ApiResponse.onSuccess("커뮤니티가 생성되었습니다."));
    }

    @Override
    public ResponseEntity<?> editCommunity(
            Long communityId, MultipartFile image,
            CommunityFormDTO communityFormDTO, String email) throws IOException {
        // 1. 회원 정보 조회
        User user = findUserByEmail(email);

        // 2. 커뮤니티 존재 확인
        Community community = findCommunityById(communityId);

        // 3. 커뮤니티 이름 중복 검사
        communityRepository.findByTitle(communityFormDTO.getTitle()).ifPresent(findCommunity -> {
            if(!community.getTitle().equals(findCommunity.getTitle()))// 원본 이름을 그대로 유지하는 경우는 상관없음
                throw new GeneralException(ErrorStatus._DUPLICATE_COMMUNITY_TITLE);
        });

        // 4. 회원이 커뮤니티 주인인지 확인
        if (!community.getOwner().equals(user)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }

        // 5. 커뮤니티 값 수정
        // 5.1. 기존 이미지 삭제
        s3Service.deleteFileByURL(community.getProfileImage());
        // 5.2. 커뮤니티 정보 변경
        community.changeCommunityInfo(communityFormDTO);
        // 5.3. 새로운 이미지 저장
        if (image!=null&&!image.isEmpty()) { // 이미지가 있을 경우 -> 새롭게 업로드
            community.changeProfileImage(s3Service.uploadFile(image));
        } else { // 이미지가 없을 경우 -> 기본 이미지
            community.changeProfileImage(null);
        }

        return ResponseEntity.ok(ApiResponse.onSuccess("커뮤니티가 수정되었습니다."));
    }

    @Override
    public ResponseEntity<?> deleteCommunity(Long communityId, String email) {
        // 1. 회원 정보 조회
        User user = findUserByEmail(email);

        // 2. 커뮤니티 존재 확인
        Community community = findCommunityById(communityId);

        // 3. 회원이 커뮤니티 주인인지 확인
        if (!community.getOwner().equals(user)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }

        // 4. 커뮤니티 이미지 삭제
        s3Service.deleteFileByURL(community.getProfileImage());
        communityRepository.delete(community);

        return ResponseEntity.ok(ApiResponse.onSuccess("커뮤니티가 삭제되었습니다."));
    }

    /**
     * 커뮤니티 가입
     */
    @Override
    public ResponseEntity<?> joinCommunity(Long communityId, String email) {
        // 1. 회원 정보 조회
        User user = findUserByEmail(email);
        // 2. 커뮤니티 존재 확인
        Community community = findCommunityById(communityId);
        // 3. 이미 커뮤니티 회원인지 확인
        if (memberRepository.findMembership(community, user).isPresent()) {
            throw new GeneralException(ErrorStatus._MEMBER_ALREADY_JOINED);
        }
        // 4. 커뮤니티 가입
        community.addMember(user); // CASCADE에 의해 member 객체 저장됨

        // 5. 채팅방 반환용 메시지 생성 & 채팅방 구독자(클라이언트)에 입장 메시지 전송
        String content = user.getPetName() + "가 " + community.getTitle() + "에 찾아왔어요";

        // 채팅방 저장용
        CommunityChat communityChat =
                CommunityChat.toCommunityChat(content, MessageType.ANNOUNCE, community, user);
        communityChatRepository.save(communityChat);

        // 채팅방 반환용 DTO
        CommunityChatResponseDTO responseMessage =
                chatConverter.toCommunityChatResponseDTO(content, MessageType.ANNOUNCE, user);
        messagingTemplate.convertAndSend("/topic/chatroom/" + communityId, responseMessage);

        return ResponseEntity.ok(ApiResponse.onSuccess("커뮤니티 가입에 성공하였습니다."));
    }

    /**
     * 커뮤니티 탈퇴(떠나기)
     */
    @Override
    public ResponseEntity<?> leaveCommunity(Long communityId, String email) {
        // 1. 회원 정보 조회
        User user = findUserByEmail(email);
        // 2. 커뮤니티 존재 확인
        Community community = findCommunityById(communityId);
        // 3. 커뮤니티 멤버 정보 조회
        Member member = memberRepository.findMembership(community, user)
                .orElseThrow(() -> new GeneralException(ErrorStatus._MEMBER_NOT_FOUND));
        // 4. 커뮤니티 탈퇴
        community.deleteMember(member);
        memberRepository.delete(member);

        // 5. 채팅방 퇴장 메시지 저장 및 반환
        String content = user.getPetName() + "가 " + community.getTitle() + "을 떠났어요.";

        // 채팅방 저장용
        CommunityChat communityChat =
                CommunityChat.toCommunityChat(content, MessageType.ANNOUNCE, community, user);
        communityChatRepository.save(communityChat);

        // 채팅방 반환용 DTO
        CommunityChatResponseDTO responseMessage =
                chatConverter.toCommunityChatResponseDTO(content, MessageType.ANNOUNCE, user);
        messagingTemplate.convertAndSend("/topic/chatroom/" + communityId, responseMessage);
        return ResponseEntity.ok(ApiResponse.onSuccess("커뮤니티 탈퇴에 성공하였습니다."));
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
