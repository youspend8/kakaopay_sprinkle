package com.kakao.kapi.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "SPRINKLE_MASTER")
@Getter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprinkleMasterEntity {
    @Id
    @Column(name = "TOKEN")
    private String token;

    @Column(name = "ROOM_ID")
    private String roomId;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "CREATE_AT")
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(name = "MONEY")
    private int money;

    @Column(name = "DIVISION")
    private int division;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "TOKEN")
    @JsonProperty("pickup_list")
    @ToString.Exclude
    private List<SprinkleDetailEntity> details;

    /**
     * 뿌리기가 마감되었는지 확인 (남은 수량이 있는가)
     * @return 마감 여부
     */
    public boolean isSoldOut() {
        return details.stream()
                .allMatch(SprinkleDetailEntity::isPicked);
    }

    /**
     * 뿌리기 유효기간이 만료되었는지 확인
     * @return 유효기간 만료 여부
     */
    public boolean isExpired() {
        return createAt.isBefore(LocalDateTime.now().minusMinutes(10));
    }

    /**
     * 뿌리기 조회 유효기간이 만료되었는지 확인
     * @return 조회 유효기간 만료 여부
     */
    public boolean isLookupExpired() {
        return createAt.isBefore(LocalDateTime.now().minusDays(7));
    }
}
