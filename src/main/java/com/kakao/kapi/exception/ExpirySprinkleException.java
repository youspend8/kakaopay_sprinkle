package com.kakao.kapi.exception;

public class ExpirySprinkleException extends SprinkleValidationException {
    public ExpirySprinkleException() {
        super("뿌린건은 10분간만 유효합니다.");
    }
}
