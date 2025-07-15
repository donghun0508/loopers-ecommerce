package com.loopers.support.error.user;

import com.loopers.support.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorType implements ErrorCode {
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, HttpStatus.CONFLICT.getReasonPhrase(), "해당 사용자 ID는 이미 사용 중입니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
