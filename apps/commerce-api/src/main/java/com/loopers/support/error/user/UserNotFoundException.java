package com.loopers.support.error.user;

import com.loopers.support.error.ErrorType;

public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
        super(ErrorType.NOT_FOUND);
    }
}
