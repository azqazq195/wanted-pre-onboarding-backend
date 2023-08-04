package com.wanted.preonboarding.user.application.mapper;

import com.wanted.preonboarding.auth.application.dto.SignUpRequest;
import com.wanted.preonboarding.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User dtoToEntity(SignUpRequest dto) {
        String encodedPassword = passwordEncoder.encode(dto.password());

        return dto.toEntity(encodedPassword);
    }
}
