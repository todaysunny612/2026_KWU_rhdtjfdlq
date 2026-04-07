package com.rhdtjfdlq_1.Cartalk_BE.service.impl;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestSignupDto;
import com.rhdtjfdlq_1.Cartalk_BE.entity.UserEntity;
import com.rhdtjfdlq_1.Cartalk_BE.repository.UserRepository;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;

    @Override
    public void signup(RequestSignupDto request) {

        // 인증 체크
        if (!redisTemplate.hasKey("auth:verified:" + request.getEmail())) {
            throw new IllegalArgumentException("이메일 인증 필요");
        }

        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 엔티티 생성
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        // 저장
        userRepository.save(user);

        // 🔥 인증 상태 제거 (추가)
        redisTemplate.delete("auth:verified:" + request.getEmail());
    }
}