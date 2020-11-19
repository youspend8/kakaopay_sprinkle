package com.kakao.kapi.exception;

import com.kakao.kapi.constants.ErrorType;

public class SprinkleNotFoundException extends SprinkleException {
    public SprinkleNotFoundException(ErrorType errorType) {
        super(errorType);
    }
}
