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

        String normalized = normalize(carNum);

        CarEntity car = carInfoRepository.findByCarNum(normalized)
                .orElseThrow(() -> new IllegalArgumentException("CAR_NOT_FOUND"));

        String ownerNickName = "알 수 없음";

        if (car.getUser() != null && car.getUser().getNickName() != null) {
            ownerNickName = car.getUser().getNickName();
        }

        return new ResponseSearchDto(
                car.getCarNum(),
                new ResponseSearchDto.Owner(ownerNickName)
        );
    }

    private String normalize(String carNum) {
        if (carNum == null) {
            return "";
        }

        return carNum.replaceAll("[^0-9가-힣]", "");
    }
}