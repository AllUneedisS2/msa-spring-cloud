package com.example.userservice.security;

import com.example.userservice.service.UserService;
import com.example.userservice.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class WebSecurityNew {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    public WebSecurityNew(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.csrf( (csrf) -> csrf.disable());

        http.authorizeHttpRequests(
                (authz) -> authz
                        .requestMatchers(
                                "/user-service/welcome"
                        ).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/user-service/users", "POST")
                        ).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/user-service/login", "POST")
                        ).permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/**").access(
                                new WebExpressionAuthorizationManager(
                                                "isAuthenticated() or " +
                                                        "hasIpAddress('127.0.0.1') or " +
                                                        "hasIpAddress('::1')"
                                )
                        )
        )
        .authenticationManager(authenticationManager)
        .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        AuthenticationFilterNew authenticationFilter =
                new AuthenticationFilterNew(authenticationManager, userService, env, jwtUtil);
        authenticationFilter.setFilterProcessesUrl("/user-service/login");

        http
                .addFilterBefore(
                        // IP 출력 필터
                        new IpAddressLoggingFilter(), SecurityContextPersistenceFilter.class
                )
                .addFilterBefore(
                        // JWT 인증 필터
                        new JwtAuthorizationFilter(authenticationManager, env, jwtUtil),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilter(
                        // 로그인 필터
                        authenticationFilter
                );

        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));

        return http.build();
    }

}
