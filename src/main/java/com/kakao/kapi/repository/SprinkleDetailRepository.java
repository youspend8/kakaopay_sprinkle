package com.kakao.kapi.repository;

import com.kakao.kapi.domain.entity.SprinkleDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprinkleDetailRepository extends JpaRepository<SprinkleDetailEntity, Long> {
}
