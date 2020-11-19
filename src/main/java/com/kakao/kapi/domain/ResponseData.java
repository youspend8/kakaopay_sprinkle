package com.kakao.kapi.domain;

import com.kakao.kapi.constants.ErrorType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseData<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ResponseData<T> valueOf(ErrorType errorType) {
        return new ResponseData<>(errorType.getCode(), errorType.getMessage(), null);
    }

    public static <T> ResponseData<T> valueOf(T data) {
        return new ResponseData<T>(ErrorType.CODE_0.getCode(), ErrorType.CODE_0.getMessage(), data);
    }
}
