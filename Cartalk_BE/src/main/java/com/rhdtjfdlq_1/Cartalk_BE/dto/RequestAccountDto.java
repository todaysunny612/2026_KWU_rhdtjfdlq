package com.rhdtjfdlq_1.Cartalk_BE.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RequestAccountDto {

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    private String phoneNumber;
}