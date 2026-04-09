package com.rhdtjfdlq_1.Cartalk_BE.service.port;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestSendMessageDto;
import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseMessageDto;

public interface MessageSendService {
    ResponseMessageDto sendMessage(Long chatId, RequestSendMessageDto request);
}