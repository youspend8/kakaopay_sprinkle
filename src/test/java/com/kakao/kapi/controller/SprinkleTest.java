package com.kakao.kapi.controller;

import com.kakao.kapi.domain.SprinkleGenerateVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class SprinkleTest extends SprinkleControllerTests {

    @Test
    @DisplayName("뿌리기 생성 테스트")
    void sprinkleTest() throws Exception {
        mockMvc.perform(post("/v1/sprinkle")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 159)
                .header("X-ROOM-ID", "ace")
                .content(objectMapper.writeValueAsString(new SprinkleGenerateVO(10000, 3))))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json; charset=UTF-8"));
    }
}
