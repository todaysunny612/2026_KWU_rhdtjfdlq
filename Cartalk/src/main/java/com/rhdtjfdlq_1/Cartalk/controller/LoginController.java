package com.rhdtjfdlq_1.Cartalk.controller;

import com.rhdtjfdlq_1.Cartalk.dto.RequestLoginDto;
import com.rhdtjfdlq_1.Cartalk.dto.ResponseLoginDto;
import com.rhdtjfdlq_1.Cartalk.service.port.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseLoginDto login(@RequestBody RequestLoginDto request) {
        return loginService.login(request);
    }
}