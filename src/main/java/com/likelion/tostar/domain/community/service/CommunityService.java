package com.likelion.tostar.domain.community.service;

import org.springframework.http.ResponseEntity;

public interface CommunityService {
    ResponseEntity<?> getRandomPreviews();
}
