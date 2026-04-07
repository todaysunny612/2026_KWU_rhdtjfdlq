package com.rhdtjfdlq_1.Cartalk_BE.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RequestProfileDto {

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String nickName;

    @NotBlank(message = "상태 메시지는 필수 입력값입니다.")
    private String message;

    @NotBlank(message = "프로필 사진은 필수 입력값입니다.")
    private String profile;
}