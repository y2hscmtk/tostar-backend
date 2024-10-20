package com.likelion.tostar.domain.community.controller;

import com.likelion.tostar.domain.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/controller")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;
}
