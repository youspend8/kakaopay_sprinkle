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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LookupTest extends SprinkleControllerTests {
    @MockBean
    private SprinkleMasterRepository sprinkleMasterRepository;

    @Test
    @DisplayName("조회 테스트 - 조회된 뿌리기 상태 return")
    void lookupTest() throws Exception {
        when(sprinkleMasterRepository.findByToken(anyString()))
                .thenReturn(mockSprinkle());

        lookupResultActions("AzF", 500, "DFS")
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("0"))
                .andExpect(jsonPath("message").value("정상적으로 처리되었습니다."))
                .andExpect(jsonPath("data").isNotEmpty());
    }

    @Test
    @DisplayName("조회 테스트 - 자신의 뿌리기가 아닐 경우")
    void lookupAccessDenyTest() throws Exception {
        when(sprinkleMasterRepository.findByToken(anyString()))
                .thenReturn(mockSprinkle());

        lookupResultActions("AzF", 999, "DFS")
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("11"))
                .andExpect(jsonPath("message").value("뿌린 사람 자신만 조회를 할 수 있습니다."));
    }

    @Test
    @DisplayName("조회 테스트 - 뿌리기 후 7일이 지난 경우")
    void lookupExpireTest() throws Exception {
        SprinkleMasterEntity sprinkleMasterEntity = mock(SprinkleMasterEntity.class);

        when(sprinkleMasterRepository.findByToken(anyString()))
                .thenReturn(sprinkleMasterEntity);
        when(sprinkleMasterEntity.isLookupExpired())
                .thenReturn(true);

        lookupResultActions("AzF", 999, "DFS")
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json; charset=UTF-8"))
                .andExpect(jsonPath("code").value("10"))
                .andExpect(jsonPath("message").value("뿌린 건에 대한 조회는 7일 동안 할 수 있습니다."));
    }

    private ResultActions lookupResultActions(String token, int userId, String roomId) throws Exception {
        return mockMvc.perform(get("/v1/sprinkle/" + token)
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
                        SprinkleDetailEntity.builder().money(473).pickupAt(LocalDateTime.now().minusMinutes(5)).userId(777).build(),
                        SprinkleDetailEntity.builder().money(292).build()
                ))
                .build();
    }
}
