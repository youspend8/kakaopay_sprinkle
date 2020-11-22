package com.kakao.kapi.domain.dto;

import lombok.*;

/**
 * 뿌리기 정보 조회 DTO
 */
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class SprinkleDetail {
    private int money;
    private int userId;
}
