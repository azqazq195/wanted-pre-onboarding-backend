package com.wanted.preonboarding.auth.application.dto;

import com.wanted.preonboarding.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @Email(regexp = ".*@.*", message = "이메일에는 '@'가 포함되어야 합니다.") String email,
        @Size(min = 8, message = "비밀번호는 최소 8자 이상입니다.") String password
) {
    public User toEntity(String encodedPassword) {
        return new User(
                email,
                encodedPassword
        );
    }
}