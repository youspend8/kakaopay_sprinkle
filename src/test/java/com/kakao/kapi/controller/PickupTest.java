package com.kakao.kapi.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PickupTest extends SprinkleControllerTests {
    @Test
    @DisplayName("뿌리기 줍기 테스트 - 줍기 성공한 뿌리기 금액 return")
    void pickupTest() throws Exception {
        pickupResultActions("gjx", 999, "ace")
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("0"));
    }

    @Test
    @DisplayName("뿌리기 줍기 테스트 - 두번 이상 줍기 시도")
    void pickupDuplicateTest() throws Exception {
        pickupResultActions("gjx", 999, "ace")
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("0"));
    }

    @Test
    @DisplayName("뿌리기 줍기 테스트 - 자신이 뿌리기 한 건일 경우")
    void pickupSelfTest() throws Exception {
        pickupResultActions("gjx", 159, "ace")
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("0"));
    }

    @Test
    @DisplayName("뿌리기 줍기 테스트 - 뿌리기가 호출된 대화방이 다를 경우")
    void pickupOtherRoomTest() throws Exception {
        pickupResultActions("gjx", 999, "dFs")
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("0"));
    }

    @Test
    @DisplayName("뿌리기 줍기 테스트 - 뿌리기 후 10분이 지난 경우")
    void pickupExpireTest() throws Exception {
        pickupResultActions("gjx", 999, "ace")
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("0"));
    }

    private ResultActions pickupResultActions(String token, int userId, String roomId) throws Exception {
        return mockMvc.perform(put("/v1/sprinkle/" + token)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId))
                .andDo(print());
    }
}
