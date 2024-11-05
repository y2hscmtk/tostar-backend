package com.likelion.tostar.domain.chat.service;

import com.likelion.tostar.domain.chat.converter.ChatConverter;
import com.likelion.tostar.domain.chat.dto.CommunityChatResponseDTO;
import com.likelion.tostar.domain.chat.entity.CommunityChat;
import com.likelion.tostar.domain.chat.repository.CommunityChatRepository;
import com.likelion.tostar.domain.community.entity.Community;
import com.likelion.tostar.domain.community.repository.CommunityRepository;
import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.exception.GeneralException;
import com.likelion.tostar.global.response.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityChatQueryServiceImpl implements CommunityChatQueryService{
    private final CommunityChatRepository communityChatRepository;
    private final CommunityRepository communityRepository;
    private final ChatConverter chatConverter;

    /**
     * 특정 채팅방 모든 채팅 반환
     */
    @Override
    public ResponseEntity<?> getAllCommunityChats(Long communityId) {
        Community community = findCommunityById(communityId);
        List<CommunityChat> communityChats = communityChatRepository.findByCommunity(community);
        ArrayList<CommunityChatResponseDTO> resultDTO = new ArrayList<>();
        for (CommunityChat communityChat : communityChats) {
            resultDTO.add(chatConverter.toCommunityChatResponseDTO(communityChat));
        }
        return ResponseEntity.ok(ApiResponse.onSuccess(resultDTO));
    }

    public Community findCommunityById(Long communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._COMMUNITY_NOT_FOUND));
    }
}
