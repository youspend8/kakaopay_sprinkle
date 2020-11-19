package com.kakao.kapi.controller.sprinkle;

import com.kakao.kapi.constants.ErrorType;
import com.kakao.kapi.domain.ResponseData;
import com.kakao.kapi.exception.SprinkleException;
import com.kakao.kapi.exception.SprinkleForbiddenException;
import com.kakao.kapi.exception.SprinkleNotFoundException;
import com.kakao.kapi.exception.SprinkleValidationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.kakao.kapi.controller.sprinkle")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SprinkleControllerAdvice {

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ResponseData<Void>> except(MissingRequestHeaderException e) {
        return ResponseEntity.badRequest()
                .body(ResponseData.valueOf(ErrorType.CODE_100));
    }

    @ExceptionHandler(SprinkleForbiddenException.class)
    public ResponseEntity<ResponseData<Void>> except(SprinkleForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ResponseData.valueOf(e.getErrorType()));
    }

    @ExceptionHandler(SprinkleNotFoundException.class)
    public ResponseEntity<ResponseData<Void>> except(SprinkleNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseData.valueOf(e.getErrorType()));
    }

    @ExceptionHandler(SprinkleValidationException.class)
    public ResponseEntity<ResponseData<Void>> except(SprinkleValidationException e) {
        return ResponseEntity.badRequest()
                .body(ResponseData.valueOf(e.getErrorType()));
    }

    @ExceptionHandler(SprinkleException.class)
    public ResponseEntity<?> except(SprinkleException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseData.valueOf(ErrorType.CODE_101));
    }
}
