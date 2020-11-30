package com.kakao.kapi.domain.dto;

import com.kakao.kapi.domain.entity.SprinkleDetailEntity;
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

    /**
     * Entity -> DTO 간 변환 정적 팩토리 메소드
     * @param sprinkleDetailEntity Entity
     * @return {@link SprinkleDetail} DTO
     */
    public static SprinkleDetail from(SprinkleDetailEntity sprinkleDetailEntity) {
        return SprinkleDetail.builder()
                .money(sprinkleDetailEntity.getMoney())
                .userId(sprinkleDetailEntity.getUserId())
                .build();
    }
}
