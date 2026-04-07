package com.rhdtjfdlq_1.Cartalk_BE.dto;

import com.rhdtjfdlq_1.Cartalk_BE.entity.CarEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResponseCarInfoDto {

    private Long carId;

    private String vehicleType;

    private String carNum;

    private String carProfile;

    private String comment;


    public static ResponseCarInfoDto from(CarEntity car) {
        return ResponseCarInfoDto.builder()
                .carId(car.getId())
                .vehicleType(car.getVehicleType())
                .carNum(car.getCarNum())
                .carProfile(car.getCarProfile())
                .comment(car.getComment())
                .build();
    }
}