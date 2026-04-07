package com.rhdtjfdlq_1.Cartalk_BE.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private String vehicleType;

    @Column(nullable = false, unique = true)
    private String carNum;

    private String carProfile;

    private String comment;

    @Column(nullable = false)
    private String registration;

    // 🔥 핵심 수정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

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

    // setter 대신 의미 있는 메서드
    public void assignUser(UserEntity user) {
        this.user = user;
    }
}