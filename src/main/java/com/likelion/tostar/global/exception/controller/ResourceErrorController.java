package com.likelion.tostar.global.exception.controller;

import com.likelion.tostar.global.enums.statuscode.ErrorStatus;
import com.likelion.tostar.global.exception.GeneralException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 잘못된 엔드포인트로 api가 요청된 경우
@RestController
public class ResourceErrorController {
    @RequestMapping("/**")
    public void handleNotFound() {
        throw new GeneralException(ErrorStatus.RESOURCE_NOT_FOUND);
    }
}

