package com.rhdtjfdlq_1.Cartalk_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResponseSearchDto {

    private String carNum;
    private Owner owner;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Owner {

        private Long userId;

        private String nickName;

        private String profile;
    }
}