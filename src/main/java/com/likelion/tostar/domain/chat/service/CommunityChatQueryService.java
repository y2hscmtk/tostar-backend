package com.likelion.tostar.domain.chat.service;

import org.springframework.http.ResponseEntity;

/**
 * GET 요청에 대한 로직은 아래에 작성
 */
public interface CommunityChatQueryService {
    ResponseEntity<?> getAllCommunityChats(Long communityId);
}
