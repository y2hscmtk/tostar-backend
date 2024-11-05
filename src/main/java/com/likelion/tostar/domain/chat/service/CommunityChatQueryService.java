package com.likelion.tostar.domain.chat.service;

import com.likelion.tostar.domain.chat.entity.CommunityChat;

/**
 * GET 요청에 대한 로직은 아래에 작성
 */
public interface CommunityChatQueryService {
    void saveChatMessage(CommunityChat chat);
}
