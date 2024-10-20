package com.likelion.tostar.domain.community.service;

import com.likelion.tostar.domain.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{
    private final CommunityRepository communityRepository;

    @Override
    public ResponseEntity<?> getRandomPreviews() {
        return null;
    }
}
