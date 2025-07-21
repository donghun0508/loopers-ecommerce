package com.loopers.support.error.user;

import com.loopers.support.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorType implements ErrorCode {
    INVALID_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), "포인트 충전 금액이 올바르지 않습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
