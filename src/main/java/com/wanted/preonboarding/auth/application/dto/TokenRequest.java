package com.wanted.preonboarding.auth.application.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank String accessToken,
        @NotBlank String refreshToken
) {
}