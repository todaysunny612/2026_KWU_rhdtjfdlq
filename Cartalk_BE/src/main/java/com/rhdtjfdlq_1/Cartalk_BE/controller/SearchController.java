package com.rhdtjfdlq_1.Cartalk_BE.controller;

import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseSearchDto;
import com.rhdtjfdlq_1.Cartalk_BE.service.port.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/cars")
    public ResponseEntity<ResponseSearchDto> searchCar(
            @RequestParam String carNum
    ) {
        ResponseSearchDto response = searchService.searchByCarNum(carNum);
        return ResponseEntity.ok(response);
    }
}