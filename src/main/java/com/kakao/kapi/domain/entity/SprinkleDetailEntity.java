package com.kakao.kapi.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SPRINKLE_DETAIL")
@Getter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprinkleDetailEntity {
    @Id
    @Column(name = "SPD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "MONEY")
    private int money;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "PICKUP_AT")
    private LocalDateTime pickupAt;

    @ManyToOne(targetEntity = SprinkleMasterEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "TOKEN")
    @ToString.Exclude
    private SprinkleMasterEntity master;

    /**
     * 줍기 처리
     * @param userId 사용자 ID
     */
    public void setPickup(int userId) {
        this.userId = userId;
        this.pickupAt = LocalDateTime.now();
    }

    /**
     * 줍기 여부 확인
     * @return 이미 주웠는지
     */
    public boolean isPicked() {
        return userId != null && pickupAt != null;
    }
}
