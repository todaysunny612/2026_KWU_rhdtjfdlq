package com.rhdtjfdlq_1.Cartalk.controller;

import com.rhdtjfdlq_1.Cartalk.dto.RequestSignupDto;
import com.rhdtjfdlq_1.Cartalk.dto.ResponseSignupDto;
import com.rhdtjfdlq_1.Cartalk.service.port.SignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseSignupDto> signup(@RequestBody @Valid RequestSignupDto request) {
        signupService.signup(request);

        return ResponseEntity.status(201)
                .body(ResponseSignupDto.message("회원가입이 완료되었습니다."));
    }
}