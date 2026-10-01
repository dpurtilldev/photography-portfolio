package com.portfolio.photographyportfolio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "photos")
public class Photo {
    
    @Id
    private String id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotBlank(message = "File name is required")
    private String fileName;
    
    @NotBlank(message = "File path is required")
    private String filePath;
    
    private String fileType;
    
    private Long fileSize;
    
    @NotNull(message = "Capture date is required")
    private LocalDateTime captureDate;
    
    private LocalDateTime uploadDate;
    
    private LocalDateTime lastEditedDate;
    
    // Camera settings
    private CameraSettings cameraSettings;
    
    // Location information
    private String location;
    
    // Tags/Categories
    private List<String> tags;
    
    // Editing software used
    private String editingSoftware;
    
    // Original vs edited flag
    private boolean isEdited;
    
    // Thumbnail path (optional)
    private String thumbnailPath;
    
    // Visibility status
    private boolean isPublic;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CameraSettings {
        private String cameraModel;
        private String lens;
        private String aperture;
        private String shutterSpeed;
        private Integer iso;
        private String focalLength;
    }
}
