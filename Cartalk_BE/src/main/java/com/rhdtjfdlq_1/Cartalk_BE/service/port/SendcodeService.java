package com.rhdtjfdlq_1.Cartalk_BE.service.port;

public interface SendcodeService {
    void sendCode(String email);
    void verifyCode(String email, String code);
}
