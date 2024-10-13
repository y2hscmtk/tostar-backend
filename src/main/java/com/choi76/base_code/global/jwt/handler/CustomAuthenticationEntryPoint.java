package com.choi76.base_code.global.jwt.handler;

import com.choi76.base_code.global.enums.statuscode.ErrorStatus;
import com.choi76.base_code.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 사용자가 JWT를 삽입하지 않았거나, 삽입한 JWT가 유효하지 않은 경우에 발생
@Slf4j(topic = "UNAUTHORIZATION_EXCEPTION_HANDLER")
@Component
@AllArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        log.error("Not Authenticated Request",authException);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.onFailure(
                        ErrorStatus.TOKEN_ERROR,
                        "JWT를 다시 한번 확인해주세요. 1. 요청 Header에 'Authorization'키로 삽입하였는지 확인, 2. Bearer 키워드를 붙였는지 확인")
        ));
    }
}
