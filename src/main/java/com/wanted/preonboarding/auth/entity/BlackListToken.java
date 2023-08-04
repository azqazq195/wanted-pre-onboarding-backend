package com.wanted.preonboarding.auth.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
@Getter
public class BlackListToken {
    @Id
    private String accessToken;
    private Long expiration;

    public BlackListToken(String accessToken, Long expiration) {
        this.accessToken = accessToken;
        this.expiration = expiration;
    }
}
