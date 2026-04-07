package com.rhdtjfdlq_1.Cartalk_BE.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RequestVerifycodeDto {
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "인증 코드는 필수입니다.")
    private String code;
}
