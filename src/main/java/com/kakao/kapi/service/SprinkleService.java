package com.kakao.kapi.service;

import com.kakao.kapi.domain.SprinkleGenerateVO;
import com.kakao.kapi.domain.dto.SprinkleMaster;

public interface SprinkleService {
    /**
     * 뿌리기
     * @param userId Http Header(X-USER-ID)
     * @param roomId Http Header(X-ROOM-ID)
     * @param sprinkleGenerate  {@link SprinkleGenerateVO}
     * @return 생성된 Token
     */
    String sprinkle(int userId, String roomId, SprinkleGenerateVO sprinkleGenerate);

    /**
     * 줍기
     * @param userId Http Header(X-USER-ID)
     * @param roomId Http Header(X-ROOM-ID)
     * @param token 뿌리기 건의 Token
     * @return 줍기 성공한 금액
     */
    int pickup(int userId, String roomId, String token);

    /**
     * 뿌리기 정보 조회
     * @param userId Http Header(X-USER-ID)
     * @param roomId Http Header(X-ROOM-ID)
     * @param token 뿌리기 건의 Token
     * @return {@link SprinkleMaster}
     */
    SprinkleMaster lookup(int userId, String roomId, String token);
}
