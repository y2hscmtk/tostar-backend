package com.likelion.tostar.domain.chat.controller;

import com.likelion.tostar.domain.chat.service.CommunityChatCommandService;
import com.likelion.tostar.domain.chat.service.CommunityChatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/chat")
public class ChatController {
    private final CommunityChatQueryService communityChatQueryService;

    @GetMapping("/{communityId}")
    public ResponseEntity<?> getAllCommunityChats(@PathVariable("communityId") Long communityId) {
        return communityChatQueryService.getAllCommunityChats(communityId);
    }
}
