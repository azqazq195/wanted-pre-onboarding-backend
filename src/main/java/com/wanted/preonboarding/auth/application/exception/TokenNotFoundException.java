package com.wanted.preonboarding.auth.application.exception;

import com.wanted.preonboarding._common.exception.ApiException;
import com.wanted.preonboarding._common.exception.ErrorCode;

public class TokenNotFoundException extends ApiException {
    public TokenNotFoundException() {
        super(ErrorCode.TOKEN_NOT_FOUND);
    }
}
