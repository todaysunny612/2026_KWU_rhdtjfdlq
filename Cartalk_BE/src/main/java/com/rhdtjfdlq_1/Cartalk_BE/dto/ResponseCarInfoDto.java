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

    private OwnerDto owner;

    public static ResponseCarInfoDto from(CarEntity car) {
        return ResponseCarInfoDto.builder()
                .carId(car.getId())
                .vehicleType(car.getVehicleType())
                .carNum(car.getCarNum())
                .carProfile(car.getCarProfile())
                .comment(car.getComment())
                .owner(
                        OwnerDto.builder()
                                .userId(car.getUser().getId())
                                .nickName(car.getUser().getNickName())
                                .profile(car.getUser().getProfile())
                                .build()
                )
                .build();
    }
    @Getter
    @AllArgsConstructor
    @Builder
    public static class OwnerDto {
        private Long userId;
        private String nickName;
        private String profile;
    }
}