package com.loopers.support.error.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorCode;

public class UserException extends CoreException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
