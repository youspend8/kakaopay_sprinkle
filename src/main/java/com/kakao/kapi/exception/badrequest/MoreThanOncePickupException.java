package com.kakao.kapi.exception.badrequest;

import com.kakao.kapi.constants.ErrorType;
import com.kakao.kapi.exception.SprinkleValidationException;

public class MoreThanOncePickupException extends SprinkleValidationException {
    public MoreThanOncePickupException() {
        super(ErrorType.CODE_22);
    }
}
