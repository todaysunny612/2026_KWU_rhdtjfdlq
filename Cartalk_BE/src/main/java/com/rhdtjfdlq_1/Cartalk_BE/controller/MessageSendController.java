package com.rhdtjfdlq_1.Cartalk_BE.controller;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestSendMessageDto;
import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseMessageDto;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.MessageSendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class MessageSendController {

    private final MessageSendService messageSendService;

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<ResponseMessageDto> sendMessage(
            @PathVariable Long chatId,
            @Valid @RequestBody RequestSendMessageDto request
    ) {
        ResponseMessageDto response = messageSendService.sendMessage(chatId, request);
        return ResponseEntity.ok(response);
    }
}