package com.rhdtjfdlq_1.Cartalk_BE.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class RequestCarInfoDto {

    private final String vehicleType;
    private final String carNum;
    private final String comment;
    private final MultipartFile carProfile;
    private final MultipartFile registration;

    public RequestCarInfoDto(
            String vehicleType,
            String carNum,
            String comment,
            MultipartFile carProfile,
            MultipartFile registration
    ) {
        this.vehicleType = vehicleType;
        this.carNum = carNum;
        this.comment = comment;
        this.carProfile = carProfile;
        this.registration = registration;
    }
}