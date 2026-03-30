package com.rhdtjfdlq_1.Cartalk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponsePasswordChangeDto {
    private String message;
    public static ResponsePasswordChangeDto success() {
        return new ResponsePasswordChangeDto("비밀번호 변경이 완료되었습니다.");
    }
}

