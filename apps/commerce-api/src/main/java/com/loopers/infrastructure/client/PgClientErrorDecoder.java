package com.loopers.infrastructure.client;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PgClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 500 && response.status() < 600) {
            return new CoreException(ErrorType.INTERNAL_ERROR, "PG 서버 내부 오류: " + response.status());
        }

        if (response.status() >= 400 && response.status() < 500) {
            return new IllegalArgumentException("잘못된 요청: " + response.status());
        }

        return new Default().decode(methodKey, response);
    }
}
