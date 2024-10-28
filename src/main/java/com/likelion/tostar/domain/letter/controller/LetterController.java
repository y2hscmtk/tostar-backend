package com.likelion.tostar.domain.letter.controller;


import com.likelion.tostar.domain.letter.dto.LetterPostRequestDto;
import com.likelion.tostar.domain.letter.service.LetterService;
import com.likelion.tostar.domain.user.dto.AddFriendDto;
import com.likelion.tostar.domain.user.dto.UserJoinDTO;
import com.likelion.tostar.global.jwt.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/letters")
@RequiredArgsConstructor
public class LetterController {
    private final LetterService letterService;

    /**
     * 편지 전송
     */
    @PostMapping()
    public ResponseEntity<?> join(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody LetterPostRequestDto letterPostRequestDto) {
        return letterService.post(customUserDetails.getId());
    }
}
