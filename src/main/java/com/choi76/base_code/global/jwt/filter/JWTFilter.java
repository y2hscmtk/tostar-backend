package com.choi76.base_code.global.jwt.filter;

import com.choi76.base_code.global.jwt.service.CustomUserDetailsService;
import com.choi76.base_code.global.jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 요청으로부터 토큰 추출 후 유효성 검증 수행
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // 요청 헤더에서 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");

        // 유효성 검증
        // 1. JWT가 헤더에 있어야 하며, Bearer 접두사로 시작해야함
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Bearer 접두사 제거
            String token = authorizationHeader.substring(7);

            // JWT 유효성 검증
            if (!jwtUtil.isExpired(token)) {
                String email = jwtUtil.getEmail(token);
                // 유저와 토큰 일치시 userDetails 생성
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                if (userDetails != null) {
                    //UserDetails, Password, Role -> 접근권한 인증 Token 생성
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    //현재 Request의 Security Context에 접근권한 설정
                    SecurityContextHolder.getContext()
                            .setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

        }

        filterChain.doFilter(request,response); // 다음 필터로

    }
}
