package com.likelion.tostar.domain.letter.service;

import org.springframework.http.ResponseEntity;

public interface LetterService {
    // 편지 생성
    ResponseEntity<?> post(Long id);
}
