package com.kakao.kapi.domain.entity.embedded;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakao.kapi.domain.entity.SprinkleDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SprinkleDetails {
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "TOKEN")
    @JsonProperty("pickup_list")
    @ToString.Exclude
    @Getter
    private List<SprinkleDetailEntity> list = new ArrayList<>();

    /**
     * 사용자가 이미 주운 뿌리기 건이 있는지 확인
     * @param userId 사용자 ID
     * @return 주운건이 있는지 ?
     */
    public boolean isExistsUser(int userId) {
        return list.stream()
                .filter(x -> x.getUserId() != null)
                .anyMatch(x -> x.getUserId() == userId);
    }

    /**
     * 줍기 처리 된 뿌리기 건만 Filtering 된 목록 반환
     * @return 줍기 처리된 뿌리기 건 List
     */
    public List<SprinkleDetailEntity> getFilteredPickedList() {
        return list.stream()
                .filter(SprinkleDetailEntity::isPicked)
                .collect(Collectors.toList());
    }

    public int size() {
        return list.size();
    }
}
