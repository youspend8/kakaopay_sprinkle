package com.kakao.kapi.exception.badrequest;

import com.kakao.kapi.constants.ErrorType;
import com.kakao.kapi.exception.SprinkleValidationException;

public class ExpirySprinkleException extends SprinkleValidationException {
    public ExpirySprinkleException() {
        super(ErrorType.CODE_20);
    }
}
