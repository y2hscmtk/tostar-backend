package com.likelion.tostar.domain.community.service;

import org.springframework.http.ResponseEntity;

/**
 * GET 요청을 제외한 다른 요청 로직 작성
 */
public interface CommunityCommandService {
    ResponseEntity<?> createCommunity(String email);

}
