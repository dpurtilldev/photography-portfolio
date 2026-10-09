package com.portfolio.photographyportfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public record PhotoUploadRequest(
        @NotNull MultipartFile file,
        @NotBlank String title,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime captureDate,
        String description,
        String location,
        List<String> tags,
        Boolean isPublic,
        Boolean isEdited,
        String cameraModel,
        String lens,
        String aperture,
        String shutterSpeed,
        Integer iso,
        String focalLength) {
}
