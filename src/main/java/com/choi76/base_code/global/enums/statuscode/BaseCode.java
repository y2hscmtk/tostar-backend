package com.choi76.base_code.global.enums.statuscode;

import org.springframework.http.HttpStatus;

// Status Base Code
public interface BaseCode {
    String getCode();
    String getMessage();
    HttpStatus getHttpStatus();
    Integer getStatusValue();
}

