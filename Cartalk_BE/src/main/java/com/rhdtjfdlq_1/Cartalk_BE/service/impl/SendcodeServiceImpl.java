package com.rhdtjfdlq_1.Cartalk_BE.service.impl;


import com.rhdtjfdlq_1.Cartalk_BE.service.port.SendcodeService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import java.time.Duration;


@Service
@RequiredArgsConstructor
public class SendcodeServiceImpl implements SendcodeService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void sendCode(String email) {

        // 재요청 제한
        if (redisTemplate.hasKey("auth:code:" + email)) {
            throw new IllegalArgumentException("이미 인증코드가 발송되었습니다. 잠시 후 다시 시도해주세요.");
        }

        // 1. 인증코드 생성
        String code = generateCode();

        // 2. Redis 저장 (TTL 5분)
        redisTemplate.opsForValue().set(
                "auth:code:" + email,
                code,
                Duration.ofMinutes(5)
        );

        // 3. 출력 (테스트용)
        System.out.println("인증코드 [" + email + "] : " + code);
    }

    //  인증 코드 검증 로직 추가
    @Override
    public void verifyCode(String email, String code) {

        String savedCode = redisTemplate.opsForValue().get("auth:code:" + email);

        // 코드 없음 → 만료
        if (savedCode == null) {
            throw new IllegalStateException("인증 코드가 만료되었습니다.");
        }

        // 코드 불일치
        if (!savedCode.equals(code)) {
            throw new IllegalArgumentException("인증 코드가 올바르지 않습니다.");
        }

        // 인증 완료 처리
        redisTemplate.opsForValue().set(
                "auth:verified:" + email,
                "true",
                Duration.ofMinutes(10)
        );

        // 기존 코드 삭제 (선택)
        redisTemplate.delete("auth:code:" + email);
    }

    // 랜덤 6자리 코드 생성
    private String generateCode() {
        int random = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(random);
    }
}