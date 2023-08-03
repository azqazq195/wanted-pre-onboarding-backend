package com.wanted.preonboding.common.environment;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String accessSecret;
    private Long accessTokenExpireTime;
    private String refreshSecret;
    private Long refreshTokenExpireTime;
}
