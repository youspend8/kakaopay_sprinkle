package com.kakao.kapi.exception;

public class AccessDeniedSprinkleException extends SprinkleValidationException {
    public AccessDeniedSprinkleException() {
        super("뿌린 사람 자신만 조회를 할 수 있습니다.");
    }
}
