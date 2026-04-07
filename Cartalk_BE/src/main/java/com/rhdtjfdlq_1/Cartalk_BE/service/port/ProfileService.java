package com.rhdtjfdlq_1.Cartalk_BE.service.port;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestProfileDto;

public interface ProfileService {
    String updateProfile(String email, RequestProfileDto request);
}