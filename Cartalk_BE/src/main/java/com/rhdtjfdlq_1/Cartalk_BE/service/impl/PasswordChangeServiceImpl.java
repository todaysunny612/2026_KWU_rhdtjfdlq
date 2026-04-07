package com.rhdtjfdlq_1.Cartalk_BE.service.impl;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestPasswordChangeDto;
import com.rhdtjfdlq_1.Cartalk_BE.entity.UserEntity;
import com.rhdtjfdlq_1.Cartalk_BE.repository.UserRepository;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.PasswordChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordChangeServiceImpl implements PasswordChangeService {

    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void changePassword(RequestPasswordChangeDto request) {

        String email = request.getEmail();
        String password = request.getPassword();
        String repassword = request.getRepassword();

        // 비밀번호 일치 여부
        if (!password.equals(repassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 이메일 인증 여부 확인 (Redis)
        if (!redisTemplate.hasKey("auth:verified:" + email)) {
            throw new IllegalArgumentException("이메일 인증이 필요합니다.");
        }

        // 사용자 조회
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 기존 비밀번호와 동일 여부
        if (user.getPassword().equals(password)) {
            throw new IllegalArgumentException("기존 비밀번호와 동일합니다.");
        }

        // 비밀번호 변경
        user.changePassword(password);

        // 저장
        userRepository.save(user);

        // 인증 상태 제거 (재사용 방지)
        redisTemplate.delete("auth:verified:" + email);
    }
}