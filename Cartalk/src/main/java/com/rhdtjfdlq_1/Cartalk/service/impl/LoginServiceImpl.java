package com.rhdtjfdlq_1.Cartalk.service.impl;

import com.rhdtjfdlq_1.Cartalk.dto.RequestLoginDto;
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
    public String login(RequestLoginDto request) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("INVALID_PASSWORD");
        }

        return "로그인 완료되었습니다.";
    }
}