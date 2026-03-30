package com.rhdtjfdlq_1.Cartalk.controller;
import com.rhdtjfdlq_1.Cartalk.dto.ResponseVerifycodeDto;
import com.rhdtjfdlq_1.Cartalk.dto.RequestVerifycodeDto;
import com.rhdtjfdlq_1.Cartalk.service.port.VerifyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class VerifyController {

    private final VerifyService verifyService;

    @PostMapping("/verify")
    public ResponseEntity<ResponseVerifycodeDto> verifyCode(
            @Valid @RequestBody RequestVerifycodeDto request) {

        // 서비스 호출
        verifyService.verifyCode(request.getEmail(), request.getCode());

        // 성공 응답
        return ResponseEntity.ok(
                ResponseVerifycodeDto.success()
        );
    }
}