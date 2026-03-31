package com.rhdtjfdlq_1.Cartalk.controller;

import com.rhdtjfdlq_1.Cartalk.dto.RequestLoginDto;
import com.rhdtjfdlq_1.Cartalk.service.port.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDto request) {
        String result = loginService.login(request);
        return ResponseEntity.ok(Map.of("return", result));
    }
}