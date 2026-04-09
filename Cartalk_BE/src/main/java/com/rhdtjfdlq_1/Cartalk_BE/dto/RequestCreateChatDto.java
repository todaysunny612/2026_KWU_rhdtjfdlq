package com.rhdtjfdlq_1.Cartalk_BE.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestCreateChatDto {
    private Long userId;
    private Long targetUserId;
}