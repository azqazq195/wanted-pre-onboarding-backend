package com.wanted.preonboarding._common.environment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@AllArgsConstructor
@Getter
public class JwtProperties {
    private String accessSecret;
    private Long accessTokenExpireTime;
    private String refreshSecret;
    private Long refreshTokenExpireTime;
}
