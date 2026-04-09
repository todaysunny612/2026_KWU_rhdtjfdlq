package com.rhdtjfdlq_1.Cartalk_BE.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestSendMessageDto {

    @NotNull(message = "userId는 필수입니다.")
    private Long userId;

    @NotBlank(message = "메시지 내용은 필수입니다.")
    private String content;

    @NotBlank(message = "messageType은 필수입니다.")
    private String messageType;
}