package com.kakao.kapi.exception;

public class InvalidTokenException extends SprinkleValidationException {
    public InvalidTokenException() {
        super("유효하지 않은 token 입니다.");
    }
}
