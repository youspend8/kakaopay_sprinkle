package com.kakao.kapi.exception;

import com.kakao.kapi.constants.ErrorType;

public class SprinkleValidationException extends SprinkleException {
    public SprinkleValidationException(ErrorType errorType) {
        super(errorType);
    }
}
