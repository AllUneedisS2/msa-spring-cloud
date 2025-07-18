package com.example.userservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;

    // 생성자: 시크릿 문자열로 HMAC-SHA 키 객체 생성
    public JwtUtil(@Value("${token.secret}") String secret) {
        // 시크릿 키를 byte 배열로 변환 후 Base64 인코딩
        byte[] keyBytes = Base64.getEncoder().encode(secret.getBytes());
        // JWT 서명을 검증하기 위해 SecretKey 객체 생성
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String createToken(String userId, long expirationMillis) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId) // 유저 식별자
                .setIssuedAt(Date.from(now)) // 생성 시각
                .setExpiration(Date.from(now.plusMillis(expirationMillis))) // 만료 시각
                .signWith(key) // HMAC 서명
                .compact();
    }

    // 토큰 검증 및 Claims 추출
    public Claims parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key) // 서명 키 설정
                .build()
                .parseClaimsJws(token) // 토큰 파싱 (서명 검증 포함)
                .getBody(); // JWT의 payload를 Claims로 받음
    }

}
