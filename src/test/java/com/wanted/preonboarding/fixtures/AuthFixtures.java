package com.wanted.preonboarding.fixtures;

import com.wanted.preonboarding.auth.application.dto.SignInRequest;
import com.wanted.preonboarding.auth.application.dto.SignUpRequest;
import com.wanted.preonboarding.auth.application.dto.TokenRequest;
import com.wanted.preonboarding.auth.application.dto.TokenResponse;
import com.wanted.preonboarding.auth.entity.Token;
import com.wanted.preonboarding.user.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;

public class AuthFixtures {
    public static User createUser() {
        return createUser("email", "password");
    }

    public static User createUser(
            String email,
            String password
    ) {
        return new User(email, password);
    }

    public static Token createToken() {
        return createToken(1L, "accessToken", "refreshToken", 1000L);
    }

    public static Token createToken(
            Long userId
    ) {
        return createToken(userId, "accessToken", "refreshToken", 1000L);
    }

    public static Token createToken(
            Long userId,
            String accessToken,
            String refreshToken,
            Long expiration
    ) {
        return new Token(userId, accessToken, refreshToken, expiration);
    }

    public static SignInRequest createSignInRequest() {
        return createSignInRequest("email@domain.com", "password");
    }

    public static SignInRequest createSignInRequest(
            String email,
            String password
    ) {
        return new SignInRequest(email, password);
    }

    public static SignUpRequest createSignUpRequest() {
        return createSignUpRequest("email@domain.com", "password");
    }

    public static SignUpRequest createSignUpRequest(
            String email,
            String password
    ) {
        return new SignUpRequest(email, password);
    }

    public static TokenRequest createTokenRequest() {
        return createTokenRequest("accessToken", "refreshToken");
    }

    public static TokenRequest createTokenRequest(Token token) {
        return createTokenRequest(token.getAccessToken(), token.getRefreshToken());
    }

    public static TokenRequest createTokenRequest(
            String accessToken,
            String refreshToken
    ) {
        return new TokenRequest(accessToken, refreshToken);
    }

    public static TokenResponse createTokenResponse() {
        return createTokenResponse("accessToken", "refreshToken");
    }

    public static TokenResponse createTokenResponse(Token token) {
        return createTokenResponse(token.getAccessToken(), token.getRefreshToken());
    }

    public static TokenResponse createTokenResponse(
            String accessToken,
            String refreshToken
    ) {
        return new TokenResponse(accessToken, refreshToken);
    }


    public static Authentication createAuthentication() {
        return new UsernamePasswordAuthenticationToken(1L, "accessToken", Collections.emptyList());
    }
}
