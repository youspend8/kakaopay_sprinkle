package com.kakao.kapi.exception.badrequest;

import com.kakao.kapi.constants.ErrorType;
import com.kakao.kapi.exception.SprinkleValidationException;

public class SoldOutSprinkleException extends SprinkleValidationException {
    public SoldOutSprinkleException() {
        super(ErrorType.CODE_24);
    }
}
