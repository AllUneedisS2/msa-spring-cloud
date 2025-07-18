package com.example.userservice.security;

import com.example.userservice.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
// 요청마다 인증에 대한 토큰 검사
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private Environment env;
    private JwtUtil jwtUtil;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  Environment env,
                                  JwtUtil jwtUtil
    ) {
        super(authenticationManager);
        this.env = env;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        String tokenHeader = request.getHeader("Authorization");

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = tokenHeader.replace("Bearer", "").trim();

        try {

            Claims claims = jwtUtil.parseToken(token);

            // JWT의 subject에 저장된 userId 값을 꺼냄
            String userId = claims.getSubject();

            if (userId != null) {
                // 인증 객체 생성 (username, credentials=null, 권한 목록=비어 있음)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());

                // 인증 정보를 Spring Security Context에 등록 → 이후 @AuthenticationPrincipal 등에서 사용 가능
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // JWT 파싱 실패 (서명 오류, 만료 등) → 인증 정보 초기화
        } catch (Exception e) {
            log.warn("JWT 검증 실패: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);

    }
}
