package com.likelion.tostar.domain.community.service;

import com.likelion.tostar.domain.community.entity.Community;
import com.likelion.tostar.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * GET 요청에 대한 로직은 아래에 작성
 */
public interface CommunityQueryService {
    ResponseEntity<?> getRandomPreviews();

    ResponseEntity<?> getAllPreviews(Pageable pageable);

    ResponseEntity<?> getMyCommunities(Pageable pageable, String email);

    ResponseEntity<?> getCommunityPreview(Long communityId);

    ResponseEntity<?> membershipCheck(Long communityId, String email);
}
