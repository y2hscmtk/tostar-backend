package com.likelion.tostar.domain.chat.service;

import com.likelion.tostar.domain.chat.entity.CommunityChat;
import com.likelion.tostar.domain.chat.repository.CommunityChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityChatQueryServiceImpl implements CommunityChatQueryService{
    private final CommunityChatRepository communityChatRepository;

    @Override
    public void saveChatMessage(CommunityChat chat) {
        communityChatRepository.save(chat);
    }
}
