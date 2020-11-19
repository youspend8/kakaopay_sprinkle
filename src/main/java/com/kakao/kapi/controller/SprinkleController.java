package com.kakao.kapi.controller;

import com.kakao.kapi.domain.SprinkleGenerateVO;
import com.kakao.kapi.domain.SprinkleMasterEntity;
import com.kakao.kapi.service.SprinkleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/sprinkle")
@RequiredArgsConstructor
public class SprinkleController {
    private final SprinkleService sprinkleService;

    /**
     * 뿌리기 건의 현재 상태 값 요청
     * @param userId Http Header(X-USER-ID)
     * @param roomId Http Header(X-ROOM-ID)
     * @param token 뿌리기 건의 Token
     * @return {@link com.kakao.kapi.domain.SprinkleMasterEntity}
     */
    @GetMapping("/{token}")
    public ResponseEntity<?> info(
            @RequestHeader("X-USER-ID") int userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @PathVariable("token") String token) {

        SprinkleMasterEntity sprinkleMasterEntity = sprinkleService.lookup(userId, roomId, token);

        return ResponseEntity.ok(sprinkleMasterEntity);
    }

    /**
     * 뿌리기 요청(생성)
     * @param userId Http Header(X-USER-ID)
     * @param roomId Http Header(X-ROOM-ID)
     * @param sprinkleGenerate {@link com.kakao.kapi.domain.SprinkleGenerateVO}
     * @return 생성된 Token
     */
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> sprinkle(
            @RequestHeader("X-USER-ID") int userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @RequestBody SprinkleGenerateVO sprinkleGenerate) {

        String token = sprinkleService.sprinkle(userId, roomId, sprinkleGenerate);

        return ResponseEntity.ok(token);
    }

    /**
     * 뿌리기 줍기
     * @param userId Http Header(X-USER-ID)
     * @param roomId Http Header(X-ROOM-ID)
     * @param token 뿌리기 건의 Token
     * @return 줍기 성공한 금액
     */
    @PutMapping("/{token}")
    public ResponseEntity<?> pickup(
            @RequestHeader("X-USER-ID") int userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @PathVariable("token") String token) {

        int money = sprinkleService.pickup(userId, roomId, token);

        return ResponseEntity.ok(money);
    }
}
