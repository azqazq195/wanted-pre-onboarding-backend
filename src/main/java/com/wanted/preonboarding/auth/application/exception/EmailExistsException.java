package com.wanted.preonboarding.auth.application.exception;

import com.wanted.preonboarding._common.exception.ApiException;
import com.wanted.preonboarding._common.exception.ErrorCode;

public class EmailExistsException extends ApiException {
    public EmailExistsException() {
        super(ErrorCode.EMAIL_EXISTS);
    }
}