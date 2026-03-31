package com.rhdtjfdlq_1.Cartalk.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class RequestCarInfoDto {

    // 차량 종류
    @NotBlank(message = "차량 종류 입력은 필수입니다.")
    private String vehicleType;

    // 차량 번호
    @NotBlank(message = "차량 번호는 필수입니다.")
    private String carNum;

    // 차량 설명 (선택)
    private String comment;

    // 차량 사진 (선택)
    private MultipartFile carProfile;

    // 차량 등록증 (필수)
    private MultipartFile registration;
}