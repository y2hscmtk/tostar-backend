package com.likelion.tostar.domain.community.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * GET 요청에 대한 로직은 아래에 작성
 */
public interface CommunityQueryService {
    ResponseEntity<?> getRandomPreviews();

    ResponseEntity<?> getAllPreviews(Pageable pageable);
}
