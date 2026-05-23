package com.rhdtjfdlq_1.Cartalk_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTopChatDto {

    private Long chatId;
    private String carNum;
    private boolean isRegisterCar;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    private Owner owner;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Owner {

        private Long userId;
        private String nickName;
        private String profile;
    }
}