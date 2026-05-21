package com.rhdtjfdlq_1.Cartalk_BE.external;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Map;

@Component
public class OcrClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String OCR_SERVER_URL = "http://localhost:8000/verify-car";

    public boolean verifyCar(String carNumber, File file) {

        try {
            System.out.println("[OCR] 요청 시작");
            System.out.println("[OCR] carNumber = " + carNumber);
            System.out.println("[OCR] file exists = " + file.exists());
            System.out.println("[OCR] file path = " + file.getAbsolutePath());
            System.out.println("[OCR] file length = " + file.length());

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("car_number", carNumber);
            body.add("file", new FileSystemResource(file));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    OCR_SERVER_URL,
                    requestEntity,
                    Map.class
            );

            System.out.println("[OCR] status = " + response.getStatusCode());
            System.out.println("[OCR] body = " + response.getBody());

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new IllegalArgumentException("OCR_SERVER_ERROR");
            }

            Map responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("match")) {
                throw new IllegalArgumentException("OCR_INVALID_RESPONSE");
            }

            return Boolean.TRUE.equals(responseBody.get("match"));

        } catch (HttpStatusCodeException e) {
            System.out.println("[OCR] HTTP 에러 상태 = " + e.getStatusCode());
            System.out.println("[OCR] HTTP 에러 응답 = " + e.getResponseBodyAsString());
            throw new IllegalArgumentException("OCR_REQUEST_FAILED: " + e.getResponseBodyAsString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("OCR_REQUEST_FAILED: " + e.getMessage());
        }
    }
}