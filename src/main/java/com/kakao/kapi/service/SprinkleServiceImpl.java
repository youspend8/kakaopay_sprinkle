package com.kakao.kapi.service;

import com.kakao.kapi.constants.ErrorType;
import com.kakao.kapi.domain.SprinkleGenerateVO;
import com.kakao.kapi.domain.dto.SprinkleDetail;
import com.kakao.kapi.domain.dto.SprinkleMaster;
import com.kakao.kapi.domain.entity.SprinkleDetailEntity;
import com.kakao.kapi.domain.entity.SprinkleMasterEntity;
import com.kakao.kapi.exception.SprinkleNotFoundException;
import com.kakao.kapi.exception.badrequest.*;
import com.kakao.kapi.exception.forbidden.AccessDeniedSprinkleException;
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
import java.util.stream.Collectors;

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
    @Transactional
    public int pickup(int userId, String roomId, String token) {
        SprinkleMasterEntity sprinkleMasterEntity = sprinkleMasterRepository.findByToken(token);

        validatePickup(sprinkleMasterEntity, userId, roomId);

        List<SprinkleDetailEntity> detailList = sprinkleMasterEntity.getDetails();

        SprinkleDetailEntity sprinkleDetailEntity = pickup(detailList);
        sprinkleDetailEntity.setPickup(userId);

        return sprinkleDetailEntity.getMoney();
    }

    private void validatePickup(SprinkleMasterEntity sprinkleMasterEntity, int userId, String roomId) {
        //  해당 Token의 뿌리기를 찾을 수 없습니다.
        if (sprinkleMasterEntity == null) {
            throw new SprinkleNotFoundException(ErrorType.CODE_102);
        }
        //  뿌린건은 10분간만 유효합니다.
        if (sprinkleMasterEntity.isExpired()) {
            throw new ExpirySprinkleException();
        }
        //  해당 뿌리기건은 마감되었습니다.
        if (sprinkleMasterEntity.isSoldOut()) {
            throw new SoldOutSprinkleException();
        }
        //  자신이 뿌리기한 건은 자신이 받을 수 없습니다.
        if (userId == sprinkleMasterEntity.getUserId()) {
            throw new PickupSelfSprinkleException();
        }
        //  뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.
        if (!roomId.equals(sprinkleMasterEntity.getRoomId())) {
            throw new NotMatchRoomException();
        }
        //  뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
        if (sprinkleMasterEntity.getDetails().stream()
                .filter(x -> x.getUserId() != null)
                .anyMatch(x -> x.getUserId() == userId)) {
            throw new MoreThanOncePickupException();
        }
    }

    private SprinkleDetailEntity pickup(List<SprinkleDetailEntity> detailList) {
        if (detailList.size() == 1) {
            return detailList.get(0);
        }
        return detailList.get(RandomNumberGenerator.generateInt(detailList.size()));
    }

    @Override
    @Transactional
    public SprinkleMaster lookup(int userId, String roomId, String token) {
        SprinkleMasterEntity sprinkleMasterEntity = sprinkleMasterRepository.findByToken(token);

        validateLookup(sprinkleMasterEntity, userId, roomId);

        //  줍기 완료된 건 목록
        List<SprinkleDetailEntity> pickedDetailEntity = sprinkleMasterEntity.getDetails().stream()
                .filter(SprinkleDetailEntity::isPicked)
                .collect(Collectors.toList());

        return SprinkleMaster.builder()
                .money(sprinkleMasterEntity.getMoney())
                .createAt(sprinkleMasterEntity.getCreateAt())
                .pickupMoney(pickedDetailEntity.stream().mapToInt(SprinkleDetailEntity::getMoney).sum())
                .pickupList(pickedDetailEntity.stream().map(entity -> SprinkleDetail.builder()
                        .money(entity.getMoney())
                        .userId(entity.getUserId())
                        .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private void validateLookup(SprinkleMasterEntity sprinkleMasterEntity, int userId, String roomId) {
        //  해당 Token의 뿌리기를 찾을 수 없습니다.
        if (sprinkleMasterEntity == null) {
            throw new SprinkleNotFoundException(ErrorType.CODE_102);
        }
        //  뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.
        if (sprinkleMasterEntity.isLookupExpired()) {
            throw new InvalidPeriodSprinkleException();
        }
        //  뿌린 사람 자신만 조회를 할 수 있습니다.
        if (userId != sprinkleMasterEntity.getUserId()) {
            throw new AccessDeniedSprinkleException();
        }
    }
}
