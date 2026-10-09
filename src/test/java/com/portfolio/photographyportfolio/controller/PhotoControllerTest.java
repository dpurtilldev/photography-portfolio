package com.portfolio.photographyportfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.portfolio.photographyportfolio.model.Photo;
import com.portfolio.photographyportfolio.service.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PhotoController.class)
@ContextConfiguration(classes = {PhotoController.class, TestWebConfig.class})
@DisplayName("PhotoController Integration Tests")
class PhotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotoService photoService;

    private ObjectMapper objectMapper;
    private Photo testPhoto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Photo.CameraSettings cameraSettings = Photo.CameraSettings.builder()
                .cameraModel("Canon EOS R5")
                .lens("RF 24-70mm F2.8")
                .aperture("f/8")
                .shutterSpeed("1/250")
                .iso(100)
                .focalLength("35mm")
                .build();

        testPhoto = Photo.builder()
                .id("1")
                .title("Sunset Beach")
                .description("Beautiful sunset at the beach")
                .fileName("sunset.jpg")
                .filePath("/photos/sunset.jpg")
                .fileType("image/jpeg")
                .fileSize(2048576L)
                .captureDate(LocalDateTime.of(2024, 8, 15, 18, 30))
                .uploadDate(LocalDateTime.of(2024, 8, 15, 19, 0))
                .location("Santa Monica Beach")
                .tags(Arrays.asList("sunset", "beach", "landscape"))
                .isEdited(true)
                .editingSoftware("Adobe Lightroom")
                .isPublic(true)
                .cameraSettings(cameraSettings)
                .build();
    }

    @Test
    @DisplayName("GET /api/photos/health should return success message")
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/photos/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Photography Portfolio API is running!"));
    }

    @Test
    @DisplayName("POST /api/photos should create new photo and return 201")
    void testCreatePhoto() throws Exception {
        // Given
        when(photoService.createPhoto(any(Photo.class))).thenReturn(testPhoto);

        // When & Then
        mockMvc.perform(post("/api/photos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPhoto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Sunset Beach"))
                .andExpect(jsonPath("$.description").value("Beautiful sunset at the beach"))
                .andExpect(jsonPath("$.fileName").value("sunset.jpg"))
                .andExpect(jsonPath("$.location").value("Santa Monica Beach"))
                .andExpect(jsonPath("$.edited").value(true))
                .andExpect(jsonPath("$.public").value(true))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasSize(3)))
                .andExpect(jsonPath("$.cameraSettings.cameraModel").value("Canon EOS R5"));

        verify(photoService, times(1)).createPhoto(any(Photo.class));
    }

    @Test
    @DisplayName("POST /api/photos with missing required fields should return 400")
    void testCreatePhotoWithMissingFields() throws Exception {
        // Given - Photo without required fields
        Photo invalidPhoto = Photo.builder()
                .description("Missing required fields")
                .build();

        // When & Then
        mockMvc.perform(post("/api/photos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPhoto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(photoService, never()).createPhoto(any(Photo.class));
    }

    @Test
    @DisplayName("GET /api/photos should return all photos")
    void testGetAllPhotos() throws Exception {
        // Given
        Photo photo2 = Photo.builder()
                .id("2")
                .title("Mountain View")
                .fileName("mountain.jpg")
                .filePath("/photos/mountain.jpg")
                .captureDate(LocalDateTime.now())
                .build();

        List<Photo> photos = Arrays.asList(testPhoto, photo2);
        when(photoService.getAllPhotos()).thenReturn(photos);

        // When & Then
        mockMvc.perform(get("/api/photos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Sunset Beach"))
                .andExpect(jsonPath("$[1].title").value("Mountain View"));

        verify(photoService, times(1)).getAllPhotos();
    }

    @Test
    @DisplayName("GET /api/photos/{id} should return photo when exists")
    void testGetPhotoByIdWhenExists() throws Exception {
        // Given
        when(photoService.getPhotoById("1")).thenReturn(Optional.of(testPhoto));

        // When & Then
        mockMvc.perform(get("/api/photos/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Sunset Beach"))
                .andExpect(jsonPath("$.location").value("Santa Monica Beach"));

        verify(photoService, times(1)).getPhotoById("1");
    }

    @Test
    @DisplayName("GET /api/photos/{id} should return 404 when photo not found")
    void testGetPhotoByIdWhenNotExists() throws Exception {
        // Given
        when(photoService.getPhotoById("999")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/photos/999"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(photoService, times(1)).getPhotoById("999");
    }

    @Test
    @DisplayName("PUT /api/photos/{id} should update photo and return 200")
    void testUpdatePhoto() throws Exception {
        // Given
        Photo updatedPhoto = Photo.builder()
                .id("1")
                .title("Updated Sunset")
                .description("Updated description")
                .fileName("sunset_updated.jpg")
                .filePath("/photos/sunset_updated.jpg")
                .captureDate(LocalDateTime.of(2024, 8, 15, 18, 30))
                .build();

        when(photoService.updatePhoto(eq("1"), any(Photo.class))).thenReturn(updatedPhoto);

        // When & Then
        mockMvc.perform(put("/api/photos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPhoto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Sunset"))
                .andExpect(jsonPath("$.description").value("Updated description"));

        verify(photoService, times(1)).updatePhoto(eq("1"), any(Photo.class));
    }

    @Test
    @DisplayName("PUT /api/photos/{id} should return 404 when photo not found")
    void testUpdatePhotoNotFound() throws Exception {
        // Given
        when(photoService.updatePhoto(eq("999"), any(Photo.class)))
                .thenThrow(new RuntimeException("Photo not found with id: 999"));

        // When & Then
        mockMvc.perform(put("/api/photos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPhoto)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(photoService, times(1)).updatePhoto(eq("999"), any(Photo.class));
    }

    @Test
    @DisplayName("DELETE /api/photos/{id} should delete photo and return 204")
    void testDeletePhoto() throws Exception {
        // Given
        doNothing().when(photoService).deletePhoto("1");

        // When & Then
        mockMvc.perform(delete("/api/photos/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(photoService, times(1)).deletePhoto("1");
    }

    @Test
    @DisplayName("GET /api/photos/search/title should search photos by title")
    void testSearchByTitle() throws Exception {
        // Given
        List<Photo> photos = Arrays.asList(testPhoto);
        when(photoService.searchByTitle("sunset")).thenReturn(photos);

        // When & Then
        mockMvc.perform(get("/api/photos/search/title")
                        .param("q", "sunset"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Sunset Beach"));

        verify(photoService, times(1)).searchByTitle("sunset");
    }

    @Test
    @DisplayName("GET /api/photos/search should search photos by title or description")
    void testSearchPhotos() throws Exception {
        // Given
        List<Photo> photos = Arrays.asList(testPhoto);
        when(photoService.searchPhotos("beautiful")).thenReturn(photos);

        // When & Then
        mockMvc.perform(get("/api/photos/search")
                        .param("q", "beautiful"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(photoService, times(1)).searchPhotos("beautiful");
    }

    @Test
    @DisplayName("GET /api/photos/tag/{tag} should return photos with specific tag")
    void testGetPhotosByTag() throws Exception {
        // Given
        List<Photo> photos = Arrays.asList(testPhoto);
        when(photoService.getPhotosByTag("beach")).thenReturn(photos);

        // When & Then
        mockMvc.perform(get("/api/photos/tag/beach"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tags", hasItem("beach")));

        verify(photoService, times(1)).getPhotosByTag("beach");
    }

    @Test
    @DisplayName("POST /api/photos/tags should return photos with all specified tags")
    void testGetPhotosByTags() throws Exception {
        // Given
        List<String> tags = Arrays.asList("sunset", "beach");
        List<Photo> photos = Arrays.asList(testPhoto);
        when(photoService.getPhotosByTags(tags)).thenReturn(photos);

        // When & Then
        mockMvc.perform(post("/api/photos/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tags)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(photoService, times(1)).getPhotosByTags(tags);
    }

    @Test
    @DisplayName("GET /api/photos/location/{location} should return photos from location")
    void testGetPhotosByLocation() throws Exception {
        // Given
        List<Photo> photos = Arrays.asList(testPhoto);
        when(photoService.getPhotosByLocation("Santa Monica")).thenReturn(photos);

        // When & Then
        mockMvc.perform(get("/api/photos/location/Santa Monica"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].location", containsString("Santa Monica")));

        verify(photoService, times(1)).getPhotosByLocation("Santa Monica");
    }

    @Test
    @DisplayName("GET /api/photos/public should return only public photos")
    void testGetPublicPhotos() throws Exception {
        // Given
        List<Photo> photos = Arrays.asList(testPhoto);
        when(photoService.getPublicPhotos()).thenReturn(photos);

        // When & Then
        mockMvc.perform(get("/api/photos/public"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].public").value(true));

        verify(photoService, times(1)).getPublicPhotos();
    }

    @Test
    @DisplayName("GET /api/photos/edited should return edited photos")
    void testGetEditedPhotos() throws Exception {
        // Given
        List<Photo> photos = Arrays.asList(testPhoto);
        when(photoService.getEditedPhotos(true)).thenReturn(photos);

        // When & Then
        mockMvc.perform(get("/api/photos/edited")
                        .param("isEdited", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].edited").value(true));

        verify(photoService, times(1)).getEditedPhotos(true);
    }

    @Test
    @DisplayName("GET /api/photos/date-range should return photos in date range")
    void testGetPhotosByDateRange() throws Exception {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 8, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 8, 31, 23, 59);
        List<Photo> photos = Arrays.asList(testPhoto);
        when(photoService.getPhotosByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(photos);

        // When & Then
        mockMvc.perform(get("/api/photos/date-range")
                        .param("start", "2024-08-01T00:00:00")
                        .param("end", "2024-08-31T23:59:59"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(photoService, times(1)).getPhotosByDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("GET /api/photos/camera/{model} should return photos by camera model")
    void testGetPhotosByCameraModel() throws Exception {
        // Given
        List<Photo> photos = Arrays.asList(testPhoto);
        when(photoService.getPhotosByCameraModel("Canon EOS R5")).thenReturn(photos);

        // When & Then
        mockMvc.perform(get("/api/photos/camera/Canon EOS R5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cameraSettings.cameraModel").value("Canon EOS R5"));

        verify(photoService, times(1)).getPhotosByCameraModel("Canon EOS R5");
    }

    @Test
    @DisplayName("GET /api/photos/recent should return recent uploads")
    void testGetRecentUploads() throws Exception {
        // Given
        List<Photo> photos = Arrays.asList(testPhoto);
        when(photoService.getRecentUploads()).thenReturn(photos);

        // When & Then
        mockMvc.perform(get("/api/photos/recent"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(photoService, times(1)).getRecentUploads();
    }
}
