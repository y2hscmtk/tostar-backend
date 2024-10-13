package com.choi76.base_code.global.enums.statuscode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum SuccessStatus implements BaseCode{

	// common
	_OK(HttpStatus.OK, "COMMON200", "성공입니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public Integer getStatusValue() {
		return httpStatus.value();
	}
}
