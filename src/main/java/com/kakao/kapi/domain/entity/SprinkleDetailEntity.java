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
    private int userId;

    @Column(name = "PICKUP_AT")
    private LocalDateTime pickupAt;

    @ManyToOne(targetEntity = SprinkleMasterEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "TOKEN")
    @ToString.Exclude
    private SprinkleMasterEntity master;
}
