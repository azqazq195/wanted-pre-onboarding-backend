package com.wanted.preonboarding.auth.utils;

import com.wanted.preonboarding.auth.application.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

public class AuthUtils {
    public static Long getLoginUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(auth)) {
            throw new UnauthorizedException();
        }
        return (Long) auth.getPrincipal();
    }
}
