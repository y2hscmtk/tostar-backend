package com.likelion.tostar.global.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.likelion.tostar.global.enums.statuscode.BaseCode;
import com.likelion.tostar.global.enums.statuscode.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 응답 형식 통일
@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    private final Boolean isSuccess;
    private final String code;

    private final String message;
    private T result;

    // 요청 성공시
    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, SuccessStatus._OK.getCode(), SuccessStatus._OK.getMessage(), result);
    }

    // 요청 실패 시
    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }
    public static <T> ApiResponse<T> onFailure(BaseCode errorCode, T data) {
        return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), data);
    }
    public static <T> ApiResponse<T> of(boolean isSuccess, BaseCode code, T result) {
        return new ApiResponse<>(isSuccess, code.getCode(), code.getMessage(), result);
    }
}
