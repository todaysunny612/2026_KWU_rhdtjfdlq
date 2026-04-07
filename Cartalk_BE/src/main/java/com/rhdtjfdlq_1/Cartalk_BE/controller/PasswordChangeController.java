package com.rhdtjfdlq_1.Cartalk_BE.controller;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestPasswordChangeDto;
import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponsePasswordChangeDto;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.PasswordChangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class PasswordChangeController {

    private final PasswordChangeService passwordChangeService;

    @PatchMapping("/password")
    public ResponseEntity<ResponsePasswordChangeDto> changePassword(
            @Valid @RequestBody RequestPasswordChangeDto request) {

        // 서비스 호출
        passwordChangeService.changePassword(request);

        // 성공 응답
        return ResponseEntity.ok(
                new ResponsePasswordChangeDto("비밀번호 변경이 완료되었습니다.")
        );
    }
}