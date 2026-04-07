package com.rhdtjfdlq_1.Cartalk_BE.service.port;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestLoginDto;

public interface LoginService {
    String login(RequestLoginDto request);
}