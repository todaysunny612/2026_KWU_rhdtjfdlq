package com.rhdtjfdlq_1.Cartalk_BE.service.port;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestCarInfoDto;
import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseCarInfoDto;

import java.util.List;

public interface CarInfoService {

    // 차량 등록
    ResponseCarInfoDto registerCar(Long userId, RequestCarInfoDto request);

    // 차량 수정
    ResponseCarInfoDto updateCar(Long userId, Long carId, RequestCarInfoDto request);

    // 내 차량 목록 조회
    List<ResponseCarInfoDto> getMyCars(Long userId);

    // 단일 차량 조회
    ResponseCarInfoDto getCar(Long userId, Long carId);

    // 차량 삭제
    void deleteCar(Long userId, Long carId);
}