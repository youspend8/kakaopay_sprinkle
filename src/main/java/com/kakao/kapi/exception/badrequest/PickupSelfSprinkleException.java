package com.kakao.kapi.exception.badrequest;

import com.kakao.kapi.constants.ErrorType;
import com.kakao.kapi.exception.SprinkleValidationException;

public class PickupSelfSprinkleException extends SprinkleValidationException {
    public PickupSelfSprinkleException(String message) {
        super(ErrorType.CODE_23);
    }
}
