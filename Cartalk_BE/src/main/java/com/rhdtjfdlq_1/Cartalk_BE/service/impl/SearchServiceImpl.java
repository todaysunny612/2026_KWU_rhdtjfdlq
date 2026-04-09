package com.rhdtjfdlq_1.Cartalk_BE.service.impl;

import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseSearchDto;
import com.rhdtjfdlq_1.Cartalk_BE.entity.CarEntity;
import com.rhdtjfdlq_1.Cartalk_BE.repository.CarInfoRepository;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final CarInfoRepository carInfoRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseSearchDto searchByCarNum(String carNum) {

        // 1. 차량번호 정규화
        String normalized = normalize(carNum);

        // 2. 조회 (없으면 예외)
        CarEntity car = carInfoRepository.findByCarNum(normalized)
                .orElseThrow(() -> new IllegalArgumentException("CAR_NOT_FOUND"));

        // 3. DTO 변환
        return new ResponseSearchDto(
                car.getCarNum(),
                new ResponseSearchDto.Owner(
                        car.getUser().getNickName()
                )
        );
    }

    // =========================
    // 차량번호 정규화
    // =========================
    private String normalize(String carNum) {
        return carNum.replaceAll("[^0-9가-힣]", "");
    }
}