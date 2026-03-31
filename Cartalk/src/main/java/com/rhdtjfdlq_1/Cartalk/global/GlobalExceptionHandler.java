package com.rhdtjfdlq_1.Cartalk.global;

import com.rhdtjfdlq_1.Cartalk.dto.ResponseSignupDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. @Valid 검증 실패 (이메일 형식, NotBlank 등)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseSignupDto> handleValidationException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity
                .badRequest()
                .body(ResponseSignupDto.error(errorMessage));
    }

    // 2. 서비스에서 던진 예외 (예: 이메일 중복)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseSignupDto> handleIllegalArgumentException(IllegalArgumentException e) {

        return ResponseEntity
                .badRequest()
                .body(ResponseSignupDto.error(e.getMessage()));
    }

    // 3. 인증 상태 관련 예외 (인증 안됨, 코드 만료 등)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseSignupDto> handleIllegalStateException(IllegalStateException e) {

        return ResponseEntity
                .badRequest()
                .body(ResponseSignupDto.error(e.getMessage()));
    }
    // 4. 로그인 예외 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {

        if (e.getMessage().equals("USER_NOT_FOUND")) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "존재하지 않는 회원입니다."));
        }

        if (e.getMessage().equals("INVALID_PASSWORD")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "이메일 또는 비밀번호가 올바르지 않습니다."));
        }

        if (e.getMessage().equals("INVALID_EMAIL_TYPE")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "올바른 이메일 타입이 아닙니다."));
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "서버 오류"));
    }
}