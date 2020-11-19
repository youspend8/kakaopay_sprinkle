package com.kakao.kapi.exception;

public class MoreThanOncePickupException extends SprinkleValidationException {
    public MoreThanOncePickupException() {
        super("뿌리기 당 한 사용자는 한번만 받을 수 있습니다.");
    }
}
