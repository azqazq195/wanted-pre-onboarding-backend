package com.wanted.preonboarding.auth.ui;

import com.wanted.preonboarding.auth.application.AuthService;
import com.wanted.preonboarding.auth.application.dto.SignInRequest;
import com.wanted.preonboarding.auth.application.dto.SignUpRequest;
import com.wanted.preonboarding.auth.application.dto.TokenRequest;
import com.wanted.preonboarding.auth.application.dto.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> signIn(
            @RequestBody @Valid SignInRequest signInRequest
    ) {
        TokenResponse response = authService.signIn(signInRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/sign-out")
    public ResponseEntity<Void> signOut(
            Authentication auth
    ) {
        authService.signOut(auth);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        authService.signUp(signUpRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @RequestBody @Valid TokenRequest tokenRequest
    ) {
        TokenResponse response = authService.refreshToken(tokenRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
