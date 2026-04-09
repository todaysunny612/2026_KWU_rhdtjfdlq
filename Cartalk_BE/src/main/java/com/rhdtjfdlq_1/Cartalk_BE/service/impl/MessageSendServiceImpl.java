package com.rhdtjfdlq_1.Cartalk_BE.service.impl;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestSendMessageDto;
import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseMessageDto;
import com.rhdtjfdlq_1.Cartalk_BE.entity.ChatMessageEntity;
import com.rhdtjfdlq_1.Cartalk_BE.entity.ChatRoomEntity;
import com.rhdtjfdlq_1.Cartalk_BE.entity.UserEntity;
import com.rhdtjfdlq_1.Cartalk_BE.repository.ChatMessageRepository;
import com.rhdtjfdlq_1.Cartalk_BE.repository.ChatRoomRepository;
import com.rhdtjfdlq_1.Cartalk_BE.repository.UserRepository;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.MessageSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageSendServiceImpl implements MessageSendService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public ResponseMessageDto sendMessage(Long chatId, RequestSendMessageDto request) {

        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("CHAT_ROOM_NOT_FOUND"));

        UserEntity sender = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        ChatMessageEntity message = chatMessageRepository.save(
                ChatMessageEntity.builder()
                        .chatRoom(chatRoom)
                        .sender(sender)
                        .content(request.getContent())
                        .messageType(request.getMessageType())
                        .build()
        );

        return ResponseMessageDto.builder()
                .messageId(message.getId())
                .senderId(sender.getId())
                .nickname(sender.getNickName())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .createdAt(message.getCreatedAt())
                .isMine(true)
                .build();
    }
}