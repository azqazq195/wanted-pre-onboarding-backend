package com.wanted.preonboarding.auth.application;

import com.wanted.preonboarding._common.environment.JwtProperties;
import com.wanted.preonboarding.auth.application.dto.TokenRequest;
import com.wanted.preonboarding.auth.application.dto.TokenResponse;
import com.wanted.preonboarding.auth.application.exception.TokenNotFoundException;
import com.wanted.preonboarding.auth.application.exception.TokenNotMatchException;
import com.wanted.preonboarding.auth.entity.BlackListToken;
import com.wanted.preonboarding.auth.entity.Token;
import com.wanted.preonboarding.auth.entity.repository.BlackListTokenRepository;
import com.wanted.preonboarding.auth.entity.repository.TokenRepository;
import com.wanted.preonboarding.user.application.exception.UserNotFoundException;
import com.wanted.preonboarding.user.entity.User;
import com.wanted.preonboarding.user.entity.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final BlackListTokenRepository blackListTokenRepository;

    private Key accessKey;
    private Key refreshKey;

    @PostConstruct
    private void init() {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getAccessSecret()));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getRefreshSecret()));
    }

    @Transactional
    public TokenResponse createToken(User user) {
        Token token = new Token(
                user.getId(),
                createAccessToken(user),
                createRefreshToken(user),
                new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpireTime()).getTime()
        );
        return new TokenResponse(tokenRepository.save(token));
    }

    @Transactional
    public void expiredToken(String accessToken) {
        setBlackList(accessToken);
        Token token = tokenRepository.findByAccessToken(accessToken).orElseThrow(TokenNotFoundException::new);
        tokenRepository.delete(token);
    }

    @Transactional
    public TokenResponse refreshToken(TokenRequest tokenRequest) {
        Token token = tokenRepository.findByRefreshToken(tokenRequest.refreshToken()).orElseThrow(TokenNotFoundException::new);
        if (!Objects.equals(token.getAccessToken(), tokenRequest.accessToken())) {
            throw new TokenNotMatchException();
        }
        expiredToken(token.getAccessToken());

        User user = userRepository.findById(token.getUserId()).orElseThrow(UserNotFoundException::new);
        return createToken(user);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaims(accessToken);
        Long userId = Long.parseLong(claims.getSubject());
        Collection<GrantedAuthority> authorities = Collections.emptyList();
        return new UsernamePasswordAuthenticationToken(userId, accessToken, authorities);
    }

    public boolean validateToken(String accessToken) {
        if (blackListTokenRepository.existsById(accessToken)) {
            log.info("로그아웃된 토큰.");
            return false;
        }

        try {
            getClaims(accessToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 토큰.");
        } catch (IllegalArgumentException e) {
            log.info("토큰 정보가 잘못되었습니다.");
        }
        return false;
    }

    protected String createAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getAccessTokenExpireTime());

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    protected String createRefreshToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getRefreshTokenExpireTime());

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    protected void setBlackList(String accessToken) {
        Claims claims = getClaims(accessToken);
        Date expiration = claims.getExpiration();
        BlackListToken blackListToken = new BlackListToken(accessToken, expiration.getTime());
        blackListTokenRepository.save(blackListToken);
    }

    protected Claims getClaims(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }
}
