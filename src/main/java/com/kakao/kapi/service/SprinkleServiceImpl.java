package com.kakao.kapi.service;

import com.kakao.kapi.domain.SprinkleGenerateVO;
import com.kakao.kapi.domain.entity.SprinkleMasterEntity;
import org.springframework.stereotype.Component;

@Component
public class SprinkleServiceImpl implements SprinkleService {
    @Override
    public String sprinkle(int userId, String roomId, SprinkleGenerateVO sprinkleGenerate) {
        return null;
    }

    @Override
    public int pickup(int userId, String roomId, String token) {
        return 0;
    }

    @Override
    public SprinkleMasterEntity lookup(int userId, String roomId, String token) {
        return null;
    }
}
