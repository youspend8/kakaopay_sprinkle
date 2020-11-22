package com.kakao.kapi.controller;

import com.kakao.kapi.domain.entity.SprinkleDetailEntity;
import com.kakao.kapi.domain.entity.SprinkleMasterEntity;
import com.kakao.kapi.repository.SprinkleMasterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PickupTest extends SprinkleControllerTests {
    @MockBean
    private SprinkleMasterRepository sprinkleMasterRepository;

    @Test
    @DisplayName("줍기 테스트 - 줍기 성공한 뿌리기 금액 return")
    void pickupTest() throws Exception {
        when(sprinkleMasterRepository.findByToken(anyString()))
                .thenReturn(mockSprinkle());

        pickupResultActions("AzF", 999, "DFS")
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("0"))
                .andExpect(jsonPath("message").value("정상적으로 처리되었습니다."))
                .andExpect(jsonPath("data").isNotEmpty());
    }

    @Test
    @DisplayName("줍기 테스트 - 두번 이상 줍기 시도")
    void pickupDuplicateTest() throws Exception {
        when(sprinkleMasterRepository.findByToken(anyString()))
                .thenReturn(mockSprinkle());

        pickupResultActions("AzF", 495, "DFS")
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("22"))
                .andExpect(jsonPath("message").value("뿌리기 당 한 사용자는 한번만 받을 수 있습니다."));
    }

    @Test
    @DisplayName("줍기 테스트 - 자신이 뿌리기 한 건일 경우")
    void pickupSelfTest() throws Exception {
        when(sprinkleMasterRepository.findByToken(anyString()))
                .thenReturn(mockSprinkle());

        pickupResultActions("AzF", 500, "DFS")
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("23"))
                .andExpect(jsonPath("message").value("자신이 뿌리기한 건은 자신이 받을 수 없습니다."));
    }

    @Test
    @DisplayName("줍기 테스트 - 뿌리기가 호출된 대화방이 다를 경우")
    void pickupOtherRoomTest() throws Exception {
        when(sprinkleMasterRepository.findByToken(anyString()))
                .thenReturn(mockSprinkle());

        pickupResultActions("AzF", 999, "dfs")
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("21"))
                .andExpect(jsonPath("message").value("뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다."));
    }

    @Test
    @DisplayName("줍기 테스트 - 뿌리기 후 10분이 지난 경우")
    void pickupExpireTest() throws Exception {
        SprinkleMasterEntity sprinkleMasterEntity = mock(SprinkleMasterEntity.class);

        when(sprinkleMasterRepository.findByToken(anyString()))
                .thenReturn(sprinkleMasterEntity);
        when(sprinkleMasterEntity.isExpired())
                .thenReturn(true);

        pickupResultActions("AzF", 999, "DFS")
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("20"))
                .andExpect(jsonPath("message").value("뿌린건은 10분간만 유효합니다."));
    }

    @Test
    @DisplayName("줍기 테스트 - 뿌리기 건이 마감되었을 경우")
    void pickupSoldOutTest() throws Exception {
        SprinkleMasterEntity sprinkleMasterEntity = mock(SprinkleMasterEntity.class);

        when(sprinkleMasterEntity.isSoldOut())
                .thenReturn(true);
        when(sprinkleMasterRepository.findByToken(anyString()))
                .thenReturn(sprinkleMasterEntity);

        pickupResultActions("AzF", 999, "DFS")
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("24"))
                .andExpect(jsonPath("message").value("해당 뿌리기건은 마감되었습니다."));
    }

    private ResultActions pickupResultActions(String token, int userId, String roomId) throws Exception {
        return mockMvc.perform(put("/v1/sprinkle/" + token)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId))
                .andDo(print());
    }

    private SprinkleMasterEntity mockSprinkle() {
        return SprinkleMasterEntity.builder()
                .token("AzF")
                .userId(500)
                .roomId("DFS")
                .division(3)
                .money(1000)
                .createAt(LocalDateTime.now())
                .details(Arrays.asList(
                        SprinkleDetailEntity.builder().money(235).pickupAt(LocalDateTime.now().minusMinutes(3)).userId(495).build(),
                        SprinkleDetailEntity.builder().money(473).build(),
                        SprinkleDetailEntity.builder().money(292).build()
                ))
                .build();
    }
}
