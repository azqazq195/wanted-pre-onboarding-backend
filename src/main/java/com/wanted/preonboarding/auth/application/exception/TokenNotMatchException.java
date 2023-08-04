package com.wanted.preonboarding.auth.application.exception;

import com.wanted.preonboarding._common.exception.ApiException;
import com.wanted.preonboarding._common.exception.ErrorCode;

public class TokenNotMatchException extends ApiException {
    public TokenNotMatchException() {
        super(ErrorCode.TOKEN_NOT_MATCH);
    }
}
