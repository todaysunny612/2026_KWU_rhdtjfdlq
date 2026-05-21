package com.rhdtjfdlq_1.Cartalk_BE.controller;

import com.rhdtjfdlq_1.Cartalk_BE.dto.ResponseImageUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping(value = "/chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseImageUploadDto uploadChatImage(
            @RequestPart("image") MultipartFile image
    ) throws IOException {

        if (image.isEmpty()) {
            throw new IllegalArgumentException("EMPTY_FILE");
        }

        String contentType = image.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg")
                        && !contentType.equals("image/png")
                        && !contentType.equals("image/jpg"))) {
            throw new IllegalArgumentException("INVALID_FILE_TYPE");
        }

        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());

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

        Files.copy(image.getInputStream(), targetPath);

        String imageUrl = "http://localhost:8080/uploads/" + fileName;

        return new ResponseImageUploadDto(imageUrl);
    }
}