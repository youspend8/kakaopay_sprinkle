package com.kakao.kapi.exception;

public class InvalidPeriodSprinkleException extends SprinkleValidationException {
    public InvalidPeriodSprinkleException() {
        super("뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.");
    }
}
