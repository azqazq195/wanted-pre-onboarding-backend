package com.wanted.preonboarding.board.application.exception;

import com.wanted.preonboarding._common.exception.ApiException;
import com.wanted.preonboarding._common.exception.ErrorCode;

public class BoardNotFoundException extends ApiException {
    public BoardNotFoundException() {
        super(ErrorCode.BOARD_NOT_FOUND);
    }
}
