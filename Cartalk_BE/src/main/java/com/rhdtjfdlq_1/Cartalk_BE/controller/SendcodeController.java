package com.rhdtjfdlq_1.Cartalk_BE.controller;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestSendcodeDto;
import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseSendcodeDto;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.SendcodeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SendcodeController {

    private final SendcodeService sendcodeService;

    @PostMapping("/code")
    public ResponseEntity<ResponseSendcodeDto> sendCode(
            @Valid @RequestBody RequestSendcodeDto request) {

        sendcodeService.sendCode(request.getEmail());

        return ResponseEntity.ok(
                new ResponseSendcodeDto("인증 코드가 발송되었습니다.")
        );
    }
}