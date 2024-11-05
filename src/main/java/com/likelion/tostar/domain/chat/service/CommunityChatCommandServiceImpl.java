package com.likelion.tostar.domain.chat.service;

import com.likelion.tostar.domain.chat.converter.ChatConverter;
import com.likelion.tostar.domain.chat.entity.ChatMessageRequestDTO;
import com.likelion.tostar.domain.chat.entity.ChatMessageResponseDTO;
import com.likelion.tostar.domain.chat.entity.CommunityChat;
import com.likelion.tostar.domain.chat.entity.enums.MessageType;
import com.likelion.tostar.domain.chat.repository.CommunityChatRepository;
import com.likelion.tostar.domain.community.entity.Community;
import com.likelion.tostar.domain.community.entity.mapping.Member;
import com.likelion.tostar.domain.community.repository.CommunityRepository;
import com.likelion.tostar.domain.community.repository.MemberRepository;
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.domain.user.repository.UserRepository;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.exception.GeneralException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityChatCommandServiceImpl implements CommunityChatCommandService{
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final CommunityChatRepository communityChatRepository;
    private final MemberRepository memberRepository;
    private final ChatConverter chatConverter;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 채팅 전송
     */
    @Override
    public void sendMessage(ChatMessageRequestDTO messageDTO, String email) {
        Community community = findCommunityById(messageDTO.getChatRoomId());
        User sender = findUserByEmail(email);
        // 채팅 생성 및 저장
        CommunityChat communityChat = CommunityChat.toCommunityChat(messageDTO.getContent(), MessageType.TALK,
                community, sender);
        communityChatRepository.save(communityChat);
        // 반환용 메시지 생성
        ChatMessageResponseDTO responseDto =
                chatConverter.toChatMessageResponseDTO(messageDTO.getContent(), MessageType.TALK, sender);
        // topic/chatroom/{chatRoomId} 를 구독한 Client 들에게 새로운 데이터 전송
        messagingTemplate.convertAndSend("/topic/chatroom/" + messageDTO.getChatRoomId(), responseDto);
    }

    /**
     * 채팅방 입장
     */
    @Override
    public void enterChatRoom(Long chatRoomId, String email) {
        Community community = findCommunityById(chatRoomId);
        User user = findUserByEmail(email);

        Optional<Member> membership = memberRepository.findMembership(community, user);

        // 이미 가입된 회원 인지 검사
        if (membership.isPresent()) {
            throw new GeneralException(ErrorStatus._MEMBER_ALREADY_JOINED);
        }
        // 새로운 회원으로 추가
        community.addMember(user);

        // 채팅방 반환용 메시지 생성 & 채팅방 구독자(클라이언트)에 입장 메시지 전송
        String content = user.getPetName() + "가 " + community.getTitle() + "에 찾아왔어요";
        // 채팅방 저장용
        CommunityChat communityChat =
                CommunityChat.toCommunityChat(content, MessageType.ANNOUNCE, community, user);
        communityChatRepository.save(communityChat);
        // 채팅방 반환용 DTO
        ChatMessageResponseDTO responseMessage =
                chatConverter.toChatMessageResponseDTO(content, MessageType.ANNOUNCE, user);

        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoomId, responseMessage);
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
