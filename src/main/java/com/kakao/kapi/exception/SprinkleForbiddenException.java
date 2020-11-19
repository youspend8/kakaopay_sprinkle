package com.kakao.kapi.exception;

import com.kakao.kapi.constants.ErrorType;

public class SprinkleForbiddenException extends SprinkleException {
    public SprinkleForbiddenException(ErrorType errorType) {
        super(errorType);
    }
}
