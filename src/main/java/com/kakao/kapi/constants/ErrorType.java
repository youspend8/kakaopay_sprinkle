package com.kakao.kapi.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorType {
    CODE_0(0, "정상적으로 처리되었습니다."),
    CODE_10(10, "뿌린 건에 대한 조회는 7일 동안 할 수 있습니다."),
    CODE_11(11, "뿌린 사람 자신만 조회를 할 수 있습니다."),
    CODE_20(20, "뿌린건은 10분간만 유효합니다."),
    CODE_21(21, "뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다."),
    CODE_22(22, "뿌리기 당 한 사용자는 한번만 받을 수 있습니다."),
    CODE_23(23, "자신이 뿌리기한 건은 자신이 받을 수 없습니다."),
    CODE_50(50, "유효하지 않은 token 입니다."),
    CODE_100(100, "요청 헤더 중 누락된 값이 존재합니다."),
    CODE_101(101, "Internal Server Error"),
    CODE_102(102, "해당 Token의 뿌리기를 찾을 수 없습니다.");

    private final int code;
    private final String message;
}
