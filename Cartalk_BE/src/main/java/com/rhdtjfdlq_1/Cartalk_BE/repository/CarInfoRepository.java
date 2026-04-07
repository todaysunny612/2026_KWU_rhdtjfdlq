package com.rhdtjfdlq_1.Cartalk_BE.repository;

import com.rhdtjfdlq_1.Cartalk_BE.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarInfoRepository extends JpaRepository<CarEntity, Long> {

    // 차량 번호로 조회
    Optional<CarEntity> findByCarNum(String carNum);

    // 차량 번호 중복 체크
    boolean existsByCarNum(String carNum);

    // 특정 유저의 차량 목록 조회
    List<CarEntity> findAllByUserId(Long userId);

    // 특정 유저의 특정 차량 조회 (보안용)
    Optional<CarEntity> findByIdAndUserId(Long carId, Long userId);
}