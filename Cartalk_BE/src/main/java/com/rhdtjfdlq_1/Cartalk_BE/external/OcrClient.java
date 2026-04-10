package com.rhdtjfdlq_1.Cartalk_BE.external;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Map;

@Component
public class OcrClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String OCR_SERVER_URL = "http://localhost:8000/verify-car";

    public boolean verifyCar(String carNumber, File file) {

        try {
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

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new IllegalArgumentException("OCR_SERVER_ERROR");
            }

            Map responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("match")) {
                throw new IllegalArgumentException("OCR_INVALID_RESPONSE");
            }

            return Boolean.TRUE.equals(responseBody.get("match"));

        } catch (Exception e) {
            throw new IllegalArgumentException("OCR_REQUEST_FAILED");
        }
    }
}