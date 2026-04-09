package com.rhdtjfdlq_1.Cartalk_BE.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity
                .badRequest()
                .body(Map.of("message", errorMessage));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {

        switch (e.getMessage()) {

            case "USER_NOT_FOUND":
            case "TARGET_USER_NOT_FOUND":
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "존재하지 않는 사용자입니다."));

            case "CHAT_ROOM_NOT_FOUND":
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "채팅방 정보가 존재하지 않습니다."));

            case "SELF_CHAT_NOT_ALLOWED":
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("message", "자기 자신과는 채팅을 시작할 수 없습니다."));

            case "INVALID_PASSWORD":
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "이메일 또는 비밀번호가 올바르지 않습니다."));

            case "INVALID_EMAIL_TYPE":
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("message", "올바른 이메일 타입이 아닙니다."));

            case "INVALID_FILE_TYPE":
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("message", "올바르지 않은 파일 형식입니다"));

            case "INVALID_PHONE_NUMBER":
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("message", "올바르지 않은 전화번호 형식입니다"));

            default:
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("message", e.getMessage()));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {

        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "서버 오류"));
    }
}