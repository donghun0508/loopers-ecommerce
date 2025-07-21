package com.loopers.support.error.user;

import com.loopers.support.error.ErrorType;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException() {
        super(ErrorType.CONFLICT);
    }
}
