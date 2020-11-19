package com.kakao.kapi.exception.notfound;

import com.kakao.kapi.constants.ErrorType;
import com.kakao.kapi.exception.SprinkleNotFoundException;

public class InvalidTokenException extends SprinkleNotFoundException {
    public InvalidTokenException() {
        super(ErrorType.CODE_50);
    }
}
