package com.rhdtjfdlq_1.Cartalk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일 (로그인 ID)
    @Column(nullable = false, unique = true)
    private String email;

    // 암호화된 비밀번호 저장
    @Column(nullable = false)
    private String password;

    // 이름
    @Column(name = "user_name", nullable = false)
    private String userName;

    // 주민번호 (보안상 암호화 or 저장 x 방향으로 진행할 예정)
    @Column(name = "user_num", nullable = false)
    private String userNum;

    // 전화번호
    @Column(name = "phone_num", nullable = false)
    private String phoneNum;

    // 계정 생성 시간
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 계정 수정 시간
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
