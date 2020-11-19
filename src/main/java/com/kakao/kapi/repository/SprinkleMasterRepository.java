package com.kakao.kapi.repository;

import com.kakao.kapi.domain.entity.SprinkleMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprinkleMasterRepository extends JpaRepository<SprinkleMasterEntity, String> {
}
