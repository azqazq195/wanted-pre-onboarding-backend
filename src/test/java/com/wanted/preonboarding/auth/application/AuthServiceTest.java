package com.wanted.preonboarding.auth.application;

import com.wanted.preonboarding.auth.application.dto.SignInRequest;
import com.wanted.preonboarding.auth.application.dto.SignUpRequest;
import com.wanted.preonboarding.auth.application.dto.TokenRequest;
import com.wanted.preonboarding.auth.application.dto.TokenResponse;
import com.wanted.preonboarding.auth.application.exception.EmailExistsException;
import com.wanted.preonboarding.auth.application.exception.PasswordNotMatchException;
import com.wanted.preonboarding.fixtures.AuthFixtures;
import com.wanted.preonboarding.user.application.exception.UserNotFoundException;
import com.wanted.preonboarding.user.application.mapper.UserMapper;
import com.wanted.preonboarding.user.entity.User;
import com.wanted.preonboarding.user.entity.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    public void signIn() {
        // given
        SignInRequest signInRequest = AuthFixtures.createSignInRequest();
        User user = AuthFixtures.createUser();
        TokenResponse tokenResponse = AuthFixtures.createTokenResponse();

        when(userRepository.findByEmail(signInRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signInRequest.password(), user.getPassword())).thenReturn(true);
        when(jwtProvider.createToken(user)).thenReturn(tokenResponse);

        // when
        TokenResponse result = authService.signIn(signInRequest);

        // then
        assertEquals(result, tokenResponse);

        verify(userRepository, times(1)).findByEmail(signInRequest.email());
        verify(passwordEncoder, times(1)).matches(signInRequest.password(), user.getPassword());
        verify(jwtProvider, times(1)).createToken(user);
    }

    @Test
    public void signIn_UserNotFound() {
        // given
        SignInRequest signInRequest = AuthFixtures.createSignInRequest();

        when(userRepository.findByEmail(signInRequest.email())).thenReturn(Optional.empty());

        // when
        assertThrows(UserNotFoundException.class, () -> authService.signIn(signInRequest));

        // then
        verify(userRepository, times(1)).findByEmail(signInRequest.email());
    }

    @Test
    public void signIn_PasswordNotMatch() {
        // given
        SignInRequest signInRequest = AuthFixtures.createSignInRequest();
        User user = AuthFixtures.createUser();

        when(userRepository.findByEmail(signInRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signInRequest.password(), user.getPassword())).thenReturn(false);

        // when
        assertThrows(PasswordNotMatchException.class, () -> authService.signIn(signInRequest));

        // then
        verify(userRepository, times(1)).findByEmail(signInRequest.email());
        verify(passwordEncoder, times(1)).matches(signInRequest.password(), user.getPassword());
    }

    @Test
    public void signOut() {
        // given
        Authentication auth = AuthFixtures.createAuthentication();
        String accessToken = (String) auth.getCredentials();

        doNothing().when(jwtProvider).expiredToken(accessToken);

        // when
        authService.signOut(auth);

        // then
        verify(jwtProvider, times(1)).expiredToken(accessToken);
    }

    @Test
    public void signUp() {
        // given
        SignUpRequest signUpRequest = AuthFixtures.createSignUpRequest();
        User user = AuthFixtures.createUser();

        when(userRepository.existsByEmail(signUpRequest.email())).thenReturn(false);
        when(userMapper.dtoToEntity(signUpRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        // when
        authService.signUp(signUpRequest);

        // then
        verify(userRepository, times(1)).existsByEmail(signUpRequest.email());
        verify(userMapper, times(1)).dtoToEntity(signUpRequest);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void signUp_EmailExists() {
        // given
        SignUpRequest signUpRequest = AuthFixtures.createSignUpRequest();

        when(userRepository.existsByEmail(signUpRequest.email())).thenReturn(true);

        // when
        assertThrows(EmailExistsException.class, () -> authService.signUp(signUpRequest));

        // then
        verify(userRepository, times(1)).existsByEmail(signUpRequest.email());
    }

    @Test
    public void refreshToken() {
        // given
        TokenRequest tokenRequest = AuthFixtures.createTokenRequest();
        TokenResponse tokenResponse = AuthFixtures.createTokenResponse();

        when(jwtProvider.refreshToken(tokenRequest)).thenReturn(tokenResponse);

        // when
        TokenResponse result = authService.refreshToken(tokenRequest);

        // then
        assertEquals(result, tokenResponse);

        verify(jwtProvider, times(1)).refreshToken(tokenRequest);
    }
}
