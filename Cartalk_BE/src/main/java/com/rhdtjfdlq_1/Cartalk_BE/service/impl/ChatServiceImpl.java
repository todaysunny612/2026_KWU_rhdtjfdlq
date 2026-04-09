package com.rhdtjfdlq_1.Cartalk_BE.service.impl;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestCreateChatDto;
import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseCreateChatDto;
import com.rhdtjfdlq_1.Cartalk_BE.entity.ChatParticipantEntity;
import com.rhdtjfdlq_1.Cartalk_BE.entity.ChatRoomEntity;
import com.rhdtjfdlq_1.Cartalk_BE.entity.UserEntity;
import com.rhdtjfdlq_1.Cartalk_BE.repository.ChatParticipantRepository;
import com.rhdtjfdlq_1.Cartalk_BE.repository.ChatRoomRepository;
import com.rhdtjfdlq_1.Cartalk_BE.repository.UserRepository;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    @Override
    @Transactional
    public ResponseCreateChatDto getOrCreateChatRoom(Long loginUserId, RequestCreateChatDto request) {

        UserEntity loginUser = userRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        UserEntity targetUser = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new IllegalArgumentException("TARGET_USER_NOT_FOUND"));

        if (loginUser.getId().equals(targetUser.getId())) {
            throw new IllegalArgumentException("SELF_CHAT_NOT_ALLOWED");
        }

        Optional<ChatRoomEntity> existingRoom =
                chatParticipantRepository.findExistingChatRoom(loginUser.getId(), targetUser.getId());

        if (existingRoom.isPresent()) {
            return ResponseCreateChatDto.builder()
                    .chatId(existingRoom.get().getId())
                    .isNew(false)
                    .build();
        }

        ChatRoomEntity newRoom = chatRoomRepository.save(
                ChatRoomEntity.builder().build()
        );

        chatParticipantRepository.save(
                ChatParticipantEntity.builder()
                        .chatRoom(newRoom)
                        .user(loginUser)
                        .build()
        );

        chatParticipantRepository.save(
                ChatParticipantEntity.builder()
                        .chatRoom(newRoom)
                        .user(targetUser)
                        .build()
        );

        return ResponseCreateChatDto.builder()
                .chatId(newRoom.getId())
                .isNew(true)
                .build();
    }
}