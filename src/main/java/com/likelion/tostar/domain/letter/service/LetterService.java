package com.likelion.tostar.domain.letter.service;

import com.likelion.tostar.domain.letter.dto.LetterPostDto;
import org.springframework.http.ResponseEntity;

public interface LetterService {
    // 편지 생성
    ResponseEntity<?> post(Long userId, LetterPostDto letterPostDto);
}
