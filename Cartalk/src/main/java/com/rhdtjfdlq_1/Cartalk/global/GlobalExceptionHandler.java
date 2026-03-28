package com.rhdtjfdlq_1.Cartalk.global;

import com.rhdtjfdlq_1.Cartalk.dto.ResponseSignupDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

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
}