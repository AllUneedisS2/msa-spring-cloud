package com.example.userservice.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@Data
@RefreshScope
public class Properties {
    @Value("${greeting.message}")
    private String message;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expiration_time}")
    private String tokenExpTime;
}
