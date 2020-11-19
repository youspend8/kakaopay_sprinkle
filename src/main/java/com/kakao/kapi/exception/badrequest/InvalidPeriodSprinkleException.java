package com.kakao.kapi.exception.badrequest;

import com.kakao.kapi.constants.ErrorType;
import com.kakao.kapi.exception.SprinkleValidationException;

public class InvalidPeriodSprinkleException extends SprinkleValidationException {
    public InvalidPeriodSprinkleException() {
        super(ErrorType.CODE_10);
    }
}
