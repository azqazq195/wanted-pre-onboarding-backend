package com.wanted.preonboarding.auth.application.dto;

import com.wanted.preonboarding.auth.entity.Token;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
    public TokenResponse(Token token) {
        this(token.getAccessToken(), token.getRefreshToken());
    }
}