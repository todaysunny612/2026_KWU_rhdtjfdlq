package com.rhdtjfdlq_1.Cartalk.service.impl;

import com.rhdtjfdlq_1.Cartalk.dto.RequestLoginDto;
import com.rhdtjfdlq_1.Cartalk.dto.ResponseLoginDto;
import com.rhdtjfdlq_1.Cartalk.entity.UserEntity;
import com.rhdtjfdlq_1.Cartalk.repository.UserRepository;
import com.rhdtjfdlq_1.Cartalk.service.port.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;

    @Override
    public ResponseLoginDto login(RequestLoginDto request) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("비밀번호 틀림");
        }

        return ResponseLoginDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getNickName())
                .build();
    }
}