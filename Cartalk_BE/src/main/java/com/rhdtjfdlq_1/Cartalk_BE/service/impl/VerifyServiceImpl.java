package com.rhdtjfdlq_1.Cartalk_BE.service.impl;

import com.rhdtjfdlq_1.Cartalk_BE.service.port.VerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class VerifyServiceImpl implements VerifyService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void verifyCode(String email, String code) {

        // Redis에서 저장된 코드 가져오기
        String savedCode = redisTemplate.opsForValue().get("auth:code:" + email);

        // 코드 없음 → 만료
        if (savedCode == null) {
            throw new IllegalStateException("인증 코드가 만료되었습니다.");
        }

        // 코드 불일치
        if (!savedCode.equals(code)) {
            throw new IllegalArgumentException("인증 코드가 올바르지 않습니다.");
        }

        // 인증 완료 처리 (verified 저장)
        redisTemplate.opsForValue().set(
                "auth:verified:" + email,
                "true",
                Duration.ofMinutes(10)
        );

        // 기존 인증코드 삭제 (선택)
        redisTemplate.delete("auth:code:" + email);
    }
}