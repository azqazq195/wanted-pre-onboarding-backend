package com.wanted.preonboarding.board.application.exception;

import com.wanted.preonboarding._common.exception.ApiException;
import com.wanted.preonboarding._common.exception.ErrorCode;

public class UnauthorizedHandleBoardException extends ApiException {
    public UnauthorizedHandleBoardException() {
        super(ErrorCode.UNAUTHORIZED_HANDLE_BOARD);
    }
}