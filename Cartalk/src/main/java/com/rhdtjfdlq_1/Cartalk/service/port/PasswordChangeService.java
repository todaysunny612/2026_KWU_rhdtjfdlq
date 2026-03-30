package com.rhdtjfdlq_1.Cartalk.service.port;

import com.rhdtjfdlq_1.Cartalk.dto.RequestPasswordChangeDto;

public interface PasswordChangeService {

    void changePassword(RequestPasswordChangeDto request);

}