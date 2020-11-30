package com.kakao.kapi.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.kakao.kapi.domain.entity.SprinkleDetailEntity;
import com.kakao.kapi.domain.entity.SprinkleMasterEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        //  줍기 완료된 건 목록
        List<SprinkleDetailEntity> pickedDetailEntity = sprinkleMasterEntity.getDetails().stream()
                .filter(SprinkleDetailEntity::isPicked)
                .collect(Collectors.toList());

        return SprinkleMaster.builder()
                .money(sprinkleMasterEntity.getMoney())
                .createAt(sprinkleMasterEntity.getCreateAt())
                .pickupMoney(pickedDetailEntity.stream().mapToInt(SprinkleDetailEntity::getMoney).sum())
                .pickupList(pickedDetailEntity.stream().map(entity -> SprinkleDetail.builder()
                        .money(entity.getMoney())
                        .userId(entity.getUserId())
                        .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
