package com.kakao.kapi.exception;

import com.kakao.kapi.constants.ErrorType;
import lombok.Getter;

@Getter
public class SprinkleException extends RuntimeException {
    protected ErrorType errorType;

    public SprinkleException(ErrorType errorType) {
        super(errorType.getMessage());
    }
}
