package com.likelion.tostar.domain.letter.service;

import com.likelion.tostar.domain.letter.dto.LetterPostRequestDto;
import org.springframework.http.ResponseEntity;

public interface LetterService {
    // 편지 생성
    ResponseEntity<?> post(Long userId, LetterPostRequestDto letterPostRequestDto);

    // 편지 목록 전체 조회
    ResponseEntity<?> searchList(Long userId, int page, int size);

    // 편지 상세 조회
    ResponseEntity<?> searchDetails(Long userId, Long letterId);
}
