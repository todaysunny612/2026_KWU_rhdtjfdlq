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

        Long userId = null;
        String ownerNickName = "알 수 없음";
        String profile = null;

        if (car.getUser() != null) {
            userId = car.getUser().getId();
            ownerNickName = car.getUser().getNickName();
            profile = car.getUser().getProfile();
        }

        return ResponseSearchDto.builder()
                .carNum(car.getCarNum())
                .owner(
                        ResponseSearchDto.Owner.builder()
                                .userId(userId)
                                .nickName(ownerNickName)
                                .profile(profile)
                                .build()
                )
                .build();
    }
    private String normalize(String carNum) {
        if (carNum == null) {
            return "";
        }

        return carNum.replaceAll("[^0-9가-힣]", "");
    }
}