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
}
