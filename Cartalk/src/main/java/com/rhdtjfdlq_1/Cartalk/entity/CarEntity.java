package com.rhdtjfdlq_1.Cartalk.entity;

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

    // 차량 종류
    @Column(nullable = false)
    private String vehicleType;

    // 차량 번호
    @Column(nullable = false, unique = true)
    private String carNum;

    // 차량 프로필 이미지 URL
    private String carProfile;

    // 차량 설명
    private String comment;

    // 차량 등록증 (파일 URL or 저장 경로)
    @Column(nullable = false)
    private String registration;

    @ManyToOne(fetch = FetchType.LAZY) //유저가 여러 차량을 등록할 수 있기 때문에 ManyToOne 구조 사용
    @JoinColumn(name = "user_id")
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

    public void setUser(UserEntity user) {
        this.user = user;
    }
}