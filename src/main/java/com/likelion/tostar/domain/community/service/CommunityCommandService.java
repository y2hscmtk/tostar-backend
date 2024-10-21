package com.likelion.tostar.domain.community.service;

import com.likelion.tostar.domain.community.dto.CommunityFormDTO;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * GET 요청을 제외한 다른 요청 로직 작성
 */
public interface CommunityCommandService {
    ResponseEntity<?> createCommunity(MultipartFile image, CommunityFormDTO communityFormDTO, String email)
            throws IOException;

    ResponseEntity<?> editCommunity(Long communityId, MultipartFile image, CommunityFormDTO communityFormDTO, String email);
}
