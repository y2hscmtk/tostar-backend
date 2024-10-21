package com.likelion.tostar.domain.community.service;

import com.likelion.tostar.domain.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityCommandServiceImpl implements CommunityCommandService {
    private final CommunityRepository communityRepository;

    @Override
    public ResponseEntity<?> createCommunity(String email) {
        return null;
    }
}
