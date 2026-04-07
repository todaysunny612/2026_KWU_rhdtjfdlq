package com.rhdtjfdlq_1.Cartalk_BE.service.port;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestAccountDto;

public interface AccountService {
    String updateMyInfo(String email, RequestAccountDto request);
}