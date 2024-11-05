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
import com.likelion.tostar.domain.user.entity.User;
import com.likelion.tostar.domain.user.repository.UserRepository;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.exception.GeneralException;
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
    private final ChatConverter chatConverter;
    private final SimpMessagingTemplate messagingTemplate;

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


    private User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
    }

    private Community findCommunityById(Long communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._COMMUNITY_NOT_FOUND));
    }
}
