package com.loopers.support.error.user;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException() {
        super(UserErrorType.USER_ALREADY_EXISTS);
    }
}
