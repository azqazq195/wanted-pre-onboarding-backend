package com.wanted.preonboarding.auth.application.exception;

import com.wanted.preonboarding._common.exception.ApiException;
import com.wanted.preonboarding._common.exception.ErrorCode;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}