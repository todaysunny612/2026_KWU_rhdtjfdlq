package com.rhdtjfdlq_1.Cartalk_BE.service.impl;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestCarInfoDto;
import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseCarInfoDto;
import com.rhdtjfdlq_1.Cartalk_BE.entity.CarEntity;
import com.rhdtjfdlq_1.Cartalk_BE.entity.UserEntity;
import com.rhdtjfdlq_1.Cartalk_BE.repository.CarInfoRepository;
import com.rhdtjfdlq_1.Cartalk_BE.repository.UserRepository;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.CarInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarInfoServiceImpl implements CarInfoService {

    private final CarInfoRepository carRepository;
    private final UserRepository userRepository;

    // 차량 등록
    @Override
    public ResponseCarInfoDto registerCar(Long userId, RequestCarInfoDto request) {

        // 유저 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        // 필수 값 검증
        if (request.getVehicleType() == null || request.getVehicleType().isBlank()) {
            throw new IllegalArgumentException("차량 종류 입력은 필수입니다.");
        }

        if (request.getCarNum() == null || request.getCarNum().isBlank()) {
            throw new IllegalArgumentException("차량 번호는 필수입니다.");
        }

        // 차량 번호 중복 체크
        if (carRepository.existsByCarNum(request.getCarNum())) {
            throw new IllegalArgumentException("이미 등록된 차량 번호입니다.");
        }

        // 등록증 필수 + 파일 타입 검증
        validateRequiredFile(request.getRegistration());

        // 프로필 파일도 타입 검사
        if (isValidFile(request.getCarProfile())) {
            validateFileType(request.getCarProfile());
        }

        // 파일 업로드
        String profileUrl = uploadFile(request.getCarProfile());
        String registrationUrl = uploadFile(request.getRegistration());

        // 엔티티 생성
        CarEntity car = CarEntity.builder()
                .vehicleType(request.getVehicleType())
                .carNum(request.getCarNum())
                .carProfile(profileUrl)
                .comment(request.getComment())
                .registration(registrationUrl)
                .user(user)
                .build();

        carRepository.save(car);

        return ResponseCarInfoDto.from(car);
    }

    // 차량 수정
    @Override
    public ResponseCarInfoDto updateCar(Long userId, Long carId, RequestCarInfoDto request) {

        CarEntity car = carRepository.findByIdAndUserId(carId, userId)
                .orElseThrow(() -> new IllegalArgumentException("차량을 찾을 수 없습니다."));

        // 🔥 vehicleType (null 방지)
        String vehicleType = car.getVehicleType();
        if (request.getVehicleType() != null && !request.getVehicleType().isBlank()) {
            vehicleType = request.getVehicleType();
        }

        // 🔥 comment (null 방지)
        String comment = car.getComment();
        if (request.getComment() != null) {
            comment = request.getComment();
        }

        // 기존 값 유지
        String profileUrl = car.getCarProfile();
        if (isValidFile(request.getCarProfile())) {
            validateFileType(request.getCarProfile()); // 🔥 추가
            profileUrl = uploadFile(request.getCarProfile());
        }

        String registrationUrl = car.getRegistration();
        if (isValidFile(request.getRegistration())) {
            validateFileType(request.getRegistration());
            registrationUrl = uploadFile(request.getRegistration());
        }

        // 엔티티 수정
        car.updateCarInfo(
                vehicleType,
                profileUrl,
                comment,
                registrationUrl
        );

        return ResponseCarInfoDto.from(car);
    }

    // 내 차량 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<ResponseCarInfoDto> getMyCars(Long userId) {

        return carRepository.findAllByUserId(userId).stream()
                .map(ResponseCarInfoDto::from)
                .toList();
    }

    // 단일 차량 조회
    @Override
    @Transactional(readOnly = true)
    public ResponseCarInfoDto getCar(Long userId, Long carId) {

        CarEntity car = carRepository.findByIdAndUserId(carId, userId)
                .orElseThrow(() -> new IllegalArgumentException("차량을 찾을 수 없습니다."));

        return ResponseCarInfoDto.from(car);
    }

    // 차량 삭제
    @Override
    public void deleteCar(Long userId, Long carId) {

        CarEntity car = carRepository.findByIdAndUserId(carId, userId)
                .orElseThrow(() -> new IllegalArgumentException("차량을 찾을 수 없습니다."));

        carRepository.delete(car);
    }

    // 파일 검증
    private void validateRequiredFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("등록증은 필수입니다.");
        }
        validateFileType(file);
    }

    private boolean isValidFile(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    private void validateFileType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null ||
                !(contentType.equals("image/png")
                        || contentType.equals("image/jpeg")
                        || contentType.equals("application/pdf"))) {

            throw new IllegalArgumentException("INVALID_FILE_TYPE");
        }
    }

    // 파일 업로드 (임시)
    private String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // TODO: S3 연동 예정
        return "https://dummy-url.com/" + file.getOriginalFilename();
    }
}