package com.rhdtjfdlq_1.Cartalk_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseSignupDto {
    private String message; //성공시 반환
    private String error; //살패시 반환

    public static ResponseSignupDto message(String message) {
        return new ResponseSignupDto(message, null);
    }

    public static ResponseSignupDto error(String error) {
        return new ResponseSignupDto(null, error);
    }

}
