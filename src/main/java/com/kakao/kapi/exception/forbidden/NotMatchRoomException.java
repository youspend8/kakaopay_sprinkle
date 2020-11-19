package com.kakao.kapi.exception.forbidden;

import com.kakao.kapi.constants.ErrorType;
import com.kakao.kapi.exception.SprinkleForbiddenException;

public class NotMatchRoomException extends SprinkleForbiddenException {
    public NotMatchRoomException() {
        super(ErrorType.CODE_21);
    }
}
