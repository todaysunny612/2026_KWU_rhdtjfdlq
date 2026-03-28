package com.rhdtjfdlq_1.Cartalk.service.impl;

import com.rhdtjfdlq_1.Cartalk.dto.RequestSignupDto;
import com.rhdtjfdlq_1.Cartalk.entity.UserEntity;
import com.rhdtjfdlq_1.Cartalk.repository.UserRepository;
import com.rhdtjfdlq_1.Cartalk.service.port.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final UserRepository userRepository;

    @Override
    public void signup(RequestSignupDto request) {

        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 2. 엔티티 생성
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        // 3. 저장
        userRepository.save(user);
    }
}