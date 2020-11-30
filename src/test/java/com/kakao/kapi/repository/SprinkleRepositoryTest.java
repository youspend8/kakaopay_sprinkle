package com.kakao.kapi.repository;

import com.kakao.kapi.domain.entity.SprinkleDetailEntity;
import com.kakao.kapi.domain.entity.SprinkleMasterEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SprinkleRepositoryTest {
    @Autowired
    private SprinkleMasterRepository sprinkleMasterRepository;

    @Autowired
    private SprinkleDetailRepository sprinkleDetailRepository;

    @ParameterizedTest
    @Order(1)
    @DisplayName("SprinkleEntity Insert 테스트")
    @CsvSource(value = {
            "abc,ZAa,594,3,10000, '3000,2000,5000'",
            "zEf,aCS,209,1,6666, '6666'",
            "DeC,BDE,658,2,122000, '45000,57000'"
    }, delimiter = ',')
    void saveTest(String token, String roomId, int userId, int division, int money, String detailMoneyStr) {
        SprinkleMasterEntity sprinkleMasterEntity = SprinkleMasterEntity.builder()
                .token(token)
                .roomId(roomId)
                .userId(userId)
                .division(division)
                .createAt(LocalDateTime.now())
                .money(money)
                .build();

        List<SprinkleDetailEntity> sprinkleDetailEntityList =
                Arrays.stream(detailMoneyStr.split(","))
                        .mapToInt(Integer::parseInt)
                        .mapToObj(x -> SprinkleDetailEntity.builder()
                                .money(x)
                                .master(sprinkleMasterEntity)
                                .build())
                        .collect(Collectors.toList());

        assertNotNull(sprinkleMasterRepository.save(sprinkleMasterEntity));
        assertNotEquals(sprinkleDetailRepository.saveAll(sprinkleDetailEntityList).size(), 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "zEf", "DeC"})
    @Order(2)
    @DisplayName("SprinkleEntity Select 테스트")
    void findTest(String token) {
        SprinkleMasterEntity sprinkleMasterEntity =
                sprinkleMasterRepository.findById(token)
                        .orElse(null);

        assertNotNull(sprinkleMasterEntity);
        assertNotEquals(sprinkleMasterEntity.getDetails().size(), 0);
    }

    @ParameterizedTest
    @Order(3)
    @ValueSource(strings = {"abc", "zEf", "DeC"})
    @DisplayName("SprinkleEntity Delete 테스트")
    public void deleteTest(String token) {
        SprinkleMasterEntity sprinkleMasterEntity = sprinkleMasterRepository.findById(token).orElse(new SprinkleMasterEntity());
        assertDoesNotThrow(() ->
                sprinkleDetailRepository.deleteAll(sprinkleMasterEntity.getDetails().getList()));
        assertDoesNotThrow(() ->
                sprinkleMasterRepository.deleteById(token));
    }
}
