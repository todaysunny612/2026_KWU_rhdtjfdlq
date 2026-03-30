package com.rhdtjfdlq_1.Cartalk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
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

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // 이름
    @Column(name = "nick_name")
    private String nickName;

    // 전화번호
    @Column(name = "phone_num")
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
