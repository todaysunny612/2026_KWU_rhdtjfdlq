package com.rhdtjfdlq_1.Cartalk_BE.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "car")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 차량 종류
    @Column(nullable = false)
    private String vehicleType;

    // 차량 번호 (유니크)
    @Column(nullable = false, unique = true)
    private String carNum;

    // 차량 프로필 이미지 URL
    private String carProfile;

    // 차량 설명
    private String comment;

    // 등록증 파일 URL (🔥 핵심: String 유지)
    @Column(nullable = false)
    private String registration;

    // 생성 시간
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 수정 시간
    private LocalDateTime updatedAt;

    // 사용자 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // =========================
    // 생성 시 실행
    // =========================
    @PrePersist
    public void prePersist() {
        this.carNum = normalize(this.carNum);
        validateCarNum(this.carNum);
        this.createdAt = LocalDateTime.now();
    }

    // =========================
    // 수정 시 실행
    // =========================
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // =========================
    // 차량번호 정규화
    // =========================
    private String normalize(String carNum) {
        return carNum.replaceAll("[^0-9가-힣]", "");
    }

    // =========================
    // 차량번호 검증
    // (2자리 또는 3자리 + 한글 + 4자리 숫자)
    // =========================
    private void validateCarNum(String carNum) {
        if (!carNum.matches("^\\d{2,3}[가-힣]\\d{4}$")) {
            throw new IllegalArgumentException("INVALID_CAR_NUM_FORMAT");
        }
    }

    // =========================
    // 비즈니스 메서드
    // =========================
    public void updateCarInfo(
            String vehicleType,
            String carProfile,
            String comment,
            String registration
    ) {
        this.vehicleType = vehicleType;
        this.carProfile = carProfile;
        this.comment = comment;
        this.registration = registration;
    }

    public void assignUser(UserEntity user) {
        this.user = user;
    }
}