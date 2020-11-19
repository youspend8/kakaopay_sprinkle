package com.kakao.kapi.exception.forbidden;

import com.kakao.kapi.constants.ErrorType;
import com.kakao.kapi.exception.SprinkleForbiddenException;

public class AccessDeniedSprinkleException extends SprinkleForbiddenException {
    public AccessDeniedSprinkleException() {
        super(ErrorType.CODE_11);
    }
}
