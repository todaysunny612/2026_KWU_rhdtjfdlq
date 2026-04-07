package com.rhdtjfdlq_1.Cartalk_BE.controller;

import com.rhdtjfdlq_1.Cartalk_BE.dto.RequestCarInfoDto;
import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseCarInfoDto;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.CarInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class CarInfoController {

    private final CarInfoService carInfoService;

    // 차량 등록
    @PostMapping("/cars")
    public ResponseEntity<?> registerCar(
            @RequestParam Long userId,
            @RequestPart("vehicleType") String vehicleType,
            @RequestPart("carNum") String carNum,
            @RequestPart(value = "comment", required = false) String comment,
            @RequestPart(value = "carProfile", required = false) MultipartFile carProfile,
            @RequestPart("registration") MultipartFile registration
    ) {
        RequestCarInfoDto request = new RequestCarInfoDto(
                vehicleType,
                carNum,
                comment,
                carProfile,
                registration
        );

        carInfoService.registerCar(userId, request);

        return ResponseEntity.ok(
                Map.of("message", "차량 등록이 완료되었습니다.")
        );
    }

    // 차량 수정
    @PatchMapping("/cars/{carId}")
    public ResponseEntity<?> updateCar(
            @RequestParam Long userId,
            @PathVariable Long carId,
            @RequestPart(value = "vehicleType", required = false) String vehicleType,
            @RequestPart(value = "comment", required = false) String comment,
            @RequestPart(value = "carProfile", required = false) MultipartFile carProfile,
            @RequestPart(value = "registration", required = false) MultipartFile registration
    ) {

        RequestCarInfoDto request = new RequestCarInfoDto(
                vehicleType,
                null, // carNum 수정 안함
                comment,
                carProfile,
                registration
        );

        carInfoService.updateCar(userId, carId, request);

        return ResponseEntity.ok(
                Map.of("message", "차량 정보 수정이 완료되었습니다.")
        );
    }

    // 내 차량 목록 조회
    @GetMapping
    public ResponseEntity<List<ResponseCarInfoDto>> getMyCars(
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(
                carInfoService.getMyCars(userId)
        );
    }

    // 단일 차량 조회
    @GetMapping("/{carId}")
    public ResponseEntity<ResponseCarInfoDto> getCar(
            @RequestParam Long userId,
            @PathVariable Long carId
    ) {
        return ResponseEntity.ok(
                carInfoService.getCar(userId, carId)
        );
    }

    // 차량 삭제
    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> deleteCar(
            @RequestParam Long userId,
            @PathVariable Long carId
    ) {
        carInfoService.deleteCar(userId, carId);
        return ResponseEntity.ok().build();
    }
}