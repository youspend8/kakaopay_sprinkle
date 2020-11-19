package com.kakao.kapi.exception;

public class PickupSelfSprinkleException extends SprinkleValidationException {
    public PickupSelfSprinkleException(String message) {
        super("자신이 뿌리기한 건은 자신이 받을 수 없습니다.");
    }
}
