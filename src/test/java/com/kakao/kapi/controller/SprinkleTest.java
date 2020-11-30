package com.kakao.kapi.controller;

import com.kakao.kapi.domain.SprinkleGenerateVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("뿌리기 생성 테스트")
public class SprinkleTest extends SprinkleControllerTests {
    @Test
    @DisplayName("뿌리기 생성 후 생성된 Token 값 return")
    void sprinkleTest() throws Exception {
        sprinkleResultActions()
                .andExpect(status().is(201))
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("0"));
    }

    private ResultActions sprinkleResultActions() throws Exception {
        return mockMvc.perform(post("/v1/sprinkle")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 159)
                .header("X-ROOM-ID", "ace")
                .content(objectMapper.writeValueAsString(new SprinkleGenerateVO(10000, 3))))
                .andDo(print());
    }
}
