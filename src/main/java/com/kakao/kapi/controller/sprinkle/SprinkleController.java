package com.kakao.kapi.controller.sprinkle;

import com.kakao.kapi.domain.ResponseData;
import com.kakao.kapi.domain.SprinkleGenerateVO;
import com.kakao.kapi.domain.dto.SprinkleMaster;
import com.kakao.kapi.domain.entity.SprinkleMasterEntity;
import com.kakao.kapi.service.SprinkleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/v1/sprinkle", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class SprinkleController {
    private final SprinkleService sprinkleService;

    /**
     * 뿌리기 건의 현재 상태 값 요청
     * @param userId Http Header(X-USER-ID)
     * @param roomId Http Header(X-ROOM-ID)
     * @param token 뿌리기 건의 Token
     * @return {@link SprinkleMasterEntity}
     */
    @GetMapping("/{token}")
    public ResponseEntity<ResponseData<SprinkleMaster>> info(
            @RequestHeader("X-USER-ID") int userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @PathVariable("token") String token) {
        log.info(">> info :: userId = {}, roomId = {}, token = {}", userId, roomId, token);

        SprinkleMaster sprinkleMaster = sprinkleService.lookup(userId, roomId, token);

        return ResponseEntity.ok(
                ResponseData.valueOf(sprinkleMaster));
    }

    /**
     * 뿌리기 요청(생성)
     * @param userId Http Header(X-USER-ID)
     * @param roomId Http Header(X-ROOM-ID)
     * @param sprinkleGenerate {@link SprinkleGenerateVO}
     * @return 생성된 Token
     */
    @PostMapping
    public ResponseEntity<ResponseData<String>> sprinkle(
            @RequestHeader("X-USER-ID") int userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @RequestBody SprinkleGenerateVO sprinkleGenerate) {
        log.info(">> sprinkle :: userId = {}, roomId = {}, params = {}", userId, roomId, sprinkleGenerate);

        String token = sprinkleService.sprinkle(userId, roomId, sprinkleGenerate);

        return ResponseEntity.created(URI.create("/v1/sprinkle/" + token))
                .body(ResponseData.valueOf(token));
    }

    /**
     * 뿌리기 줍기
     * @param userId Http Header(X-USER-ID)
     * @param roomId Http Header(X-ROOM-ID)
     * @param token 뿌리기 건의 Token
     * @return 줍기 성공한 금액
     */
    @PutMapping("/{token}")
    public ResponseEntity<ResponseData<Integer>> pickup(
            @RequestHeader("X-USER-ID") int userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @PathVariable("token") String token) {
        log.info(">> pickup :: userId = {}, roomId = {}, token = {}", userId, roomId, token);

        int money = sprinkleService.pickup(userId, roomId, token);

        return ResponseEntity.ok(
                ResponseData.valueOf(money));
    }
}
