package com.rhdtjfdlq_1.Cartalk_BE.service.impl;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestProfileDto;
import com.rhdtjfdlq_1.Cartalk_BE.entity.UserEntity;
import com.rhdtjfdlq_1.Cartalk_BE.repository.UserRepository;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public String updateProfile(String email, RequestProfileDto request) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        if (request.getProfile() != null && !request.getProfile().isBlank()) {
            String profile = request.getProfile().toLowerCase();
            boolean validFileType =
                            profile.endsWith(".png") ||
                            profile.endsWith(".jpg") ||
                            profile.endsWith(".jpeg") ||
                            profile.endsWith(".pdf");

            if (!validFileType) {
                throw new RuntimeException("INVALID_FILE_TYPE");
            }
        }

        user.updateProfile(
                request.getNickName(),
                request.getMessage(),
                request.getProfile()
        );

        return "프로필 설정이 완료되었습니다.";
    }
}