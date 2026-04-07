package com.rhdtjfdlq_1.Cartalk_BE.controller;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestAccountDto;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AccountController {

    private final AccountService myInfoService;

    @PatchMapping("/account/{email}")
    public ResponseEntity<?> updateMyInfo(
            @PathVariable String email,
            @Valid @RequestBody RequestAccountDto request
    ) {
        String result = myInfoService.updateMyInfo(email, request);
        return ResponseEntity.ok(Map.of("message", result));
    }
}