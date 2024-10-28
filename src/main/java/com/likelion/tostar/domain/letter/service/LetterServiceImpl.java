package com.likelion.tostar.domain.letter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService{
    @Override
    public ResponseEntity<?> post(Long id) {
        return null;
    }
}
