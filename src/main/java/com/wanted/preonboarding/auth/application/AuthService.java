package com.wanted.preonboarding.auth.application;

import com.wanted.preonboarding.auth.application.dto.SignInRequest;
import com.wanted.preonboarding.auth.application.dto.SignUpRequest;
import com.wanted.preonboarding.auth.application.dto.TokenRequest;
import com.wanted.preonboarding.auth.application.dto.TokenResponse;
import com.wanted.preonboarding.auth.application.exception.EmailExistsException;
import com.wanted.preonboarding.auth.application.exception.PasswordNotMatchException;
import com.wanted.preonboarding.user.application.exception.UserNotFoundException;
import com.wanted.preonboarding.user.application.mapper.UserMapper;
import com.wanted.preonboarding.user.entity.User;
import com.wanted.preonboarding.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    @Transactional
    public TokenResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.findByEmail(signInRequest.email()).orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            throw new PasswordNotMatchException();
        }
        return jwtProvider.createToken(user);
    }

    @Transactional
    public void signOut(Authentication auth) {
        String accessToken = (String) auth.getCredentials();
        jwtProvider.expiredToken(accessToken);
    }

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new EmailExistsException();
        }
        User user = userMapper.dtoToEntity(signUpRequest);
        userRepository.save(user);
    }

    @Transactional
    public TokenResponse refreshToken(TokenRequest tokenRequest) {
        return jwtProvider.refreshToken(tokenRequest);
    }
}
