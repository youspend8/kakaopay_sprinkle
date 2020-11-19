package com.kakao.kapi.exception;

public class NotMatchRoomException extends SprinkleValidationException {
    public NotMatchRoomException() {
        super("뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.");
    }
}
