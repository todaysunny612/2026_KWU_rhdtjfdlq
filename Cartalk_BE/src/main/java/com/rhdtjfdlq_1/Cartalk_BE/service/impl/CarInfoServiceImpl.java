package com.rhdtjfdlq_1.Cartalk_BE.service.impl;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestCarInfoDto;
import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseCarInfoDto;
import com.rhdtjfdlq_1.Cartalk_BE.entity.CarEntity;
import com.rhdtjfdlq_1.Cartalk_BE.entity.UserEntity;
import com.rhdtjfdlq_1.Cartalk_BE.external.OcrClient;
import com.rhdtjfdlq_1.Cartalk_BE.repository.CarInfoRepository;
import com.rhdtjfdlq_1.Cartalk_BE.repository.UserRepository;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.CarInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CarInfoServiceImpl implements CarInfoService {

    private final CarInfoRepository carRepository;
    private final UserRepository userRepository;
    private final OcrClient ocrClient;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 차량 등록
    @Override
    public ResponseCarInfoDto registerCar(Long userId, RequestCarInfoDto request) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        if (request.getVehicleType() == null || request.getVehicleType().isBlank()) {
            throw new IllegalArgumentException("차량 종류 입력은 필수입니다.");
        }

        if (request.getCarNum() == null || request.getCarNum().isBlank()) {
            throw new IllegalArgumentException("차량 번호는 필수입니다.");
        }

        if (carRepository.existsByCarNum(request.getCarNum())) {
            throw new IllegalArgumentException("이미 등록된 차량 번호입니다.");
        }

        // 등록증 필수 + 파일 타입 검증
        validateRequiredFile(request.getRegistration());

        // OCR 검증
        File tempFile = convertToFile(request.getRegistration());

        boolean isValid = ocrClient.verifyCar(
                request.getCarNum(),
                tempFile
        );

        tempFile.delete();

        if (!isValid) {
            throw new IllegalArgumentException("CAR_VERIFICATION_FAILED");
        }

        // 차량 프로필 사진 타입 검증
        if (isValidFile(request.getCarProfile())) {
            validateFileType(request.getCarProfile());
        }

        // 실제 파일 저장
        String profileUrl = uploadFile(request.getCarProfile());
        String registrationUrl = uploadFile(request.getRegistration());

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

        String vehicleType = car.getVehicleType();
        if (request.getVehicleType() != null && !request.getVehicleType().isBlank()) {
            vehicleType = request.getVehicleType();
        }

        String comment = car.getComment();
        if (request.getComment() != null) {
            comment = request.getComment();
        }

        String profileUrl = car.getCarProfile();
        if (isValidFile(request.getCarProfile())) {
            validateFileType(request.getCarProfile());
            profileUrl = uploadFile(request.getCarProfile());
        }

        String registrationUrl = car.getRegistration();
        if (isValidFile(request.getRegistration())) {
            validateFileType(request.getRegistration());
            registrationUrl = uploadFile(request.getRegistration());
        }

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

    // MultipartFile → OCR 검증용 임시 File 변환
    private File convertToFile(MultipartFile multipartFile) {
        try {
            String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

            if (extension == null || extension.isBlank()) {
                extension = "tmp";
            }

            File file = File.createTempFile("upload-", "." + extension);

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            return file;

        } catch (Exception e) {
            throw new IllegalArgumentException("FILE_CONVERT_FAILED");
        }
    }

    // 등록증 필수 검증
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
                        || contentType.equals("image/jpg")
                        || contentType.equals("application/pdf"))) {

            throw new IllegalArgumentException("INVALID_FILE_TYPE");
        }
    }

    // 실제 파일 저장
    private String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

            if (extension == null || extension.isBlank()) {
                throw new IllegalArgumentException("INVALID_FILE_EXTENSION");
            }

            String fileName = UUID.randomUUID() + "." + extension;

            Path uploadPath = Paths.get(uploadDir)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(uploadPath);

            Path targetPath = uploadPath
                    .resolve(fileName)
                    .normalize();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return "http://localhost:8080/uploads/" + fileName;

        } catch (Exception e) {
            throw new IllegalArgumentException("FILE_UPLOAD_FAILED");
        }
    }
}