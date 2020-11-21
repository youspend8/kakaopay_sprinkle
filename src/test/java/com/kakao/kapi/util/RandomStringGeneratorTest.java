package com.kakao.kapi.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class RandomStringGeneratorTest {

    @RepeatedTest(500)
    @DisplayName("랜덤 문자열 생성 테스트")
    void generateTest() {
        assertDoesNotThrow(() -> RandomStringGenerator.generator(3));
    }
}
