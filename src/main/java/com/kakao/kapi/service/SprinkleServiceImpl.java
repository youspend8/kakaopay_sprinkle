package com.kakao.kapi.service;

import com.kakao.kapi.constants.ErrorType;
import com.kakao.kapi.domain.SprinkleGenerateVO;
import com.kakao.kapi.domain.entity.SprinkleDetailEntity;
import com.kakao.kapi.domain.entity.SprinkleMasterEntity;
import com.kakao.kapi.exception.SprinkleNotFoundException;
import com.kakao.kapi.exception.badrequest.ExpirySprinkleException;
import com.kakao.kapi.exception.badrequest.MoreThanOncePickupException;
import com.kakao.kapi.exception.badrequest.PickupSelfSprinkleException;
import com.kakao.kapi.exception.forbidden.NotMatchRoomException;
import com.kakao.kapi.repository.SprinkleDetailRepository;
import com.kakao.kapi.repository.SprinkleMasterRepository;
import com.kakao.kapi.util.RandomNumberGenerator;
import com.kakao.kapi.util.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SprinkleServiceImpl implements SprinkleService {
    private final SprinkleMasterRepository sprinkleMasterRepository;
    private final SprinkleDetailRepository sprinkleDetailRepository;

    @Override
    @Transactional
    public String sprinkle(int userId, String roomId, SprinkleGenerateVO sprinkleGenerate) {
        SprinkleMasterEntity sprinkleMasterEntity = SprinkleMasterEntity.builder()
                .token(RandomStringGenerator.generator(3))
                .division(sprinkleGenerate.getDivision())
                .money(sprinkleGenerate.getMoney())
                .userId(userId)
                .roomId(roomId)
                .createAt(LocalDateTime.now())
                .build();

        sprinkleMasterRepository.save(sprinkleMasterEntity);

        int[] moneyArr = divide(sprinkleGenerate);

        List<SprinkleDetailEntity> detailList = new ArrayList<>();
        for (int money : moneyArr) {
            SprinkleDetailEntity sprinkleDetailEntity = SprinkleDetailEntity.builder()
                    .money(money)
                    .master(sprinkleMasterEntity)
                    .build();
            detailList.add(sprinkleDetailEntity);
        }

        sprinkleDetailRepository.saveAll(detailList);

        return sprinkleMasterEntity.getToken();
    }

    /**
     * 뿌리기 금액 나누기
     * @param sprinkleGenerate {@link SprinkleGenerateVO}
     * @return 나누어진 금액들의 배열
     */
    private int[] divide(SprinkleGenerateVO sprinkleGenerate) {
        int length = sprinkleGenerate.getDivision();
        int[] result = new int[length];
        int minus = length - 1;         //  나눌 금액에서 0원으로 나누어지는 경우를 제외하기 위하여 존재
                                        //  예를들어 나눌 금액 : 1000, 나눌 수 : 3 일 때 첫 차례 나눌때는 최대금액이 998원이 되어야 함.
        int max = sprinkleGenerate.getMoney() - minus;

        for (int i = 0; i < length - 1; i++) {
            int dividedMoney = RandomNumberGenerator.generateInt(max - minus--);
            result[i] = dividedMoney;
            max -= dividedMoney;
        }
        result[length - 1] = max + (length - 1);
        return result;
    }

    @Override
    public int pickup(int userId, String roomId, String token) {
        SprinkleMasterEntity sprinkleMasterEntity = sprinkleMasterRepository.findById(token)
                .orElseThrow(() -> new SprinkleNotFoundException(ErrorType.CODE_102));
        //  자신이 뿌리기한 건은 자신이 받을 수 없습니다.
        if (userId == sprinkleMasterEntity.getUserId()) {
            throw new PickupSelfSprinkleException();
        }
        //  뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.
        if (!roomId.equals(sprinkleMasterEntity.getRoomId())) {
            throw new NotMatchRoomException();
        }
        //  뿌린건은 10분간만 유효합니다.
        if (sprinkleMasterEntity.getCreateAt().isBefore(LocalDateTime.now().minusMinutes(10))) {
            throw new ExpirySprinkleException();
        }
        //  뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
        if (sprinkleMasterEntity.getDetails().stream()
                .filter(x -> x.getUserId() != null)
                .anyMatch(x -> x.getUserId() == userId)) {
            throw new MoreThanOncePickupException();
        }

        List<SprinkleDetailEntity> detailList = sprinkleMasterEntity.getDetails();

        SprinkleDetailEntity sprinkleDetailEntity = pickup(detailList);

        return sprinkleDetailEntity.getMoney();
    }

    private SprinkleDetailEntity pickup(List<SprinkleDetailEntity> detailList) {
        if (detailList.size() == 1) {
            return detailList.get(0);
        }
        return detailList.get(RandomNumberGenerator.generateInt(detailList.size()));
    }

    @Override
    public SprinkleMasterEntity lookup(int userId, String roomId, String token) {
        return null;
    }
}
