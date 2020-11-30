package com.kakao.kapi.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.kakao.kapi.domain.entity.SprinkleMasterEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 뿌리기 정보 조회 DTO
 */
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class SprinkleMaster {
    private int money;

    private int pickupMoney;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createAt;

    private List<SprinkleDetail> pickupList;

    /**
     * Entity -> DTO 간 변환 정적 팩토리 메소드
     * @param sprinkleMasterEntity Entity
     * @return {@link SprinkleMaster} DTO
     */
    public static SprinkleMaster from(SprinkleMasterEntity sprinkleMasterEntity) {
        return SprinkleMaster.builder()
                .money(sprinkleMasterEntity.getMoney())
                .createAt(sprinkleMasterEntity.getCreateAt())
                .pickupMoney(sprinkleMasterEntity.getDetails().getFilteredPickedSum())
                .pickupList(sprinkleMasterEntity.getDetails().getFilteredPickedDtoList())
                .build();
    }
}
