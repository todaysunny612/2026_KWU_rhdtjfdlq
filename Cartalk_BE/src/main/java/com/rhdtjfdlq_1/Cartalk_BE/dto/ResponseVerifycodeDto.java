package com.rhdtjfdlq_1.Cartalk_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseVerifycodeDto {
    private String message;

    public static ResponseVerifycodeDto success() {
        return new ResponseVerifycodeDto("이메일 인증이 완료되었습니다.");
    }
    public static ResponseVerifycodeDto error(String message){
        return new ResponseVerifycodeDto(message);
    }
}
