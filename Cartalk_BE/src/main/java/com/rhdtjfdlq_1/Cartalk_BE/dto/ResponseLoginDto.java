package com.rhdtjfdlq_1.Cartalk_BE.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseLoginDto {
    private Long id;
    private String email;
    private String userName;
}