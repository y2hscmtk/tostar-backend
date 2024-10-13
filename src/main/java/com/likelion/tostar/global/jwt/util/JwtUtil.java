package com.likelion.tostar.global.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// JWTUtil 0.12.3
/**
 * <reference>
 * https://sjh9708.tistory.com/170
 * https://www.youtube.com/playlist?list=PLJkjrxxiBSFCcOjy0AAVGNtIa08VLk1EJ
 */
@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long tokenExpTime; // access token expiration time

    // Secret Key
    public JwtUtil(@Value("${spring.jwt.secret}") String secret,
                   @Value("${spring.jwt.expiration_time}") long tokenExpTime) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.tokenExpTime = tokenExpTime;
    }

    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // 시크릿 키를 사용하여 복호화한다.
                .build() // 복호화에 사용할 Jwts Parser 생성
                // 생성된 Parser를 사용하여 token으로 부터 정보 추출
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmail(String token) {
        return getAllClaims(token).get("email", String.class);
    }

    public String getRole(String token) {
        return getAllClaims(token).get("role", String.class);
    }

    // 토큰이 만료되었는지 확인, 오늘 날짜와 토큰에 저장된 만료일자를 비교한다.
    // 만료되었다면 true
    public Boolean isExpired(String token) {
        return getAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // 비즈니스 로직에 따라 claim 수정 필요
    public String createJwt(String email, String role) {
        return Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExpTime))
                .signWith(secretKey)
                .compact();
    }
}

