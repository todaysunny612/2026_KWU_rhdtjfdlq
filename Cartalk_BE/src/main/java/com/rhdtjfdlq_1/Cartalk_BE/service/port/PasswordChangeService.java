package com.rhdtjfdlq_1.Cartalk_BE.service.port;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestPasswordChangeDto;

public interface PasswordChangeService {

    void changePassword(RequestPasswordChangeDto request);

}