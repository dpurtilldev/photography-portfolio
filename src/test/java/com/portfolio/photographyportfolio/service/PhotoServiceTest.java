package com.portfolio.photographyportfolio.service;

import com.portfolio.photographyportfolio.model.Photo;
import com.portfolio.photographyportfolio.repository.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PhotoService Unit Tests")
class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private PhotoService photoService;

    private Photo testPhoto;
    private Photo.CameraSettings cameraSettings;

    @BeforeEach
    void setUp() {
        cameraSettings = Photo.CameraSettings.builder()
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
                .location("Santa Monica Beach")
                .tags(Arrays.asList("sunset", "beach", "landscape"))
                .isEdited(true)
                .editingSoftware("Adobe Lightroom")
                .isPublic(true)
                .cameraSettings(cameraSettings)
                .build();
    }

    @Test
    @DisplayName("Should create photo successfully")
    void testCreatePhoto() {
        // Given
        when(photoRepository.save(any(Photo.class))).thenReturn(testPhoto);

        // When
        Photo createdPhoto = photoService.createPhoto(testPhoto);

        // Then
        assertThat(createdPhoto).isNotNull();
        assertThat(createdPhoto.getTitle()).isEqualTo("Sunset Beach");
        assertThat(createdPhoto.getUploadDate()).isNotNull();
        verify(photoRepository, times(1)).save(any(Photo.class));
    }

    @Test
    @DisplayName("Should set upload date when creating photo")
    void testCreatePhotoSetsUploadDate() {
        // Given
        Photo photoWithoutUploadDate = Photo.builder()
                .title("Test Photo")
                .fileName("test.jpg")
                .filePath("/test.jpg")
                .captureDate(LocalDateTime.now())
                .build();

        when(photoRepository.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Photo result = photoService.createPhoto(photoWithoutUploadDate);

        // Then
        assertThat(result.getUploadDate()).isNotNull();
    }

    @Test
    @DisplayName("Should set last edited date for edited photos")
    void testCreatePhotoSetsLastEditedDateForEditedPhotos() {
        // Given
        Photo editedPhoto = Photo.builder()
                .title("Edited Photo")
                .fileName("edited.jpg")
                .filePath("/edited.jpg")
                .captureDate(LocalDateTime.now())
                .isEdited(true)
                .build();

        when(photoRepository.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Photo result = photoService.createPhoto(editedPhoto);

        // Then
        assertThat(result.getLastEditedDate()).isNotNull();
    }

    @Test
    @DisplayName("Should get all photos")
    void testGetAllPhotos() {
        // Given
        Photo photo2 = Photo.builder()
                .id("2")
                .title("Mountain View")
                .fileName("mountain.jpg")
                .filePath("/photos/mountain.jpg")
                .captureDate(LocalDateTime.now())
                .build();

        List<Photo> expectedPhotos = Arrays.asList(testPhoto, photo2);
        when(photoRepository.findAll()).thenReturn(expectedPhotos);

        // When
        List<Photo> actualPhotos = photoService.getAllPhotos();

        // Then
        assertThat(actualPhotos).hasSize(2);
        assertThat(actualPhotos).containsExactlyElementsOf(expectedPhotos);
        verify(photoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get photo by ID when photo exists")
    void testGetPhotoByIdWhenExists() {
        // Given
        when(photoRepository.findById("1")).thenReturn(Optional.of(testPhoto));

        // When
        Optional<Photo> result = photoService.getPhotoById("1");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Sunset Beach");
        verify(photoRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Should return empty when photo ID does not exist")
    void testGetPhotoByIdWhenNotExists() {
        // Given
        when(photoRepository.findById("999")).thenReturn(Optional.empty());

        // When
        Optional<Photo> result = photoService.getPhotoById("999");

        // Then
        assertThat(result).isEmpty();
        verify(photoRepository, times(1)).findById("999");
    }

    @Test
    @DisplayName("Should update photo successfully")
    void testUpdatePhoto() {
        // Given
        Photo updatedDetails = Photo.builder()
                .title("Updated Sunset")
                .description("Updated description")
                .fileName("sunset_updated.jpg")
                .filePath("/photos/sunset_updated.jpg")
                .fileType("image/jpeg")
                .fileSize(3048576L)
                .captureDate(LocalDateTime.of(2024, 8, 15, 18, 30))
                .tags(Arrays.asList("sunset", "beach", "updated"))
                .isEdited(true)
                .editingSoftware("Photoshop")
                .isPublic(false)
                .build();

        when(photoRepository.findById("1")).thenReturn(Optional.of(testPhoto));
        when(photoRepository.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Photo result = photoService.updatePhoto("1", updatedDetails);

        // Then
        assertThat(result.getTitle()).isEqualTo("Updated Sunset");
        assertThat(result.getDescription()).isEqualTo("Updated description");
        assertThat(result.getEditingSoftware()).isEqualTo("Photoshop");
        assertThat(result.isPublic()).isFalse();
        assertThat(result.getLastEditedDate()).isNotNull();
        verify(photoRepository, times(1)).findById("1");
        verify(photoRepository, times(1)).save(any(Photo.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent photo")
    void testUpdatePhotoNotFound() {
        // Given
        when(photoRepository.findById("999")).thenReturn(Optional.empty());

        Photo updatedDetails = Photo.builder()
                .title("Updated Title")
                .fileName("test.jpg")
                .filePath("/test.jpg")
                .captureDate(LocalDateTime.now())
                .build();

        // When & Then
        assertThatThrownBy(() -> photoService.updatePhoto("999", updatedDetails))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Photo not found with id: 999");

        verify(photoRepository, times(1)).findById("999");
        verify(photoRepository, never()).save(any(Photo.class));
    }

    @Test
    @DisplayName("Should delete photo successfully")
    void testDeletePhoto() {
        // Given
        doNothing().when(photoRepository).deleteById("1");

        // When
        photoService.deletePhoto("1");

        // Then
        verify(photoRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Should search photos by title")
    void testSearchByTitle() {
        // Given
        List<Photo> expectedPhotos = Arrays.asList(testPhoto);
        when(photoRepository.findByTitleContainingIgnoreCase("sunset")).thenReturn(expectedPhotos);

        // When
        List<Photo> result = photoService.searchByTitle("sunset");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).containsIgnoringCase("sunset");
        verify(photoRepository, times(1)).findByTitleContainingIgnoreCase("sunset");
    }

    @Test
    @DisplayName("Should get photos by tag")
    void testGetPhotosByTag() {
        // Given
        List<Photo> expectedPhotos = Arrays.asList(testPhoto);
        when(photoRepository.findByTagsContaining("beach")).thenReturn(expectedPhotos);

        // When
        List<Photo> result = photoService.getPhotosByTag("beach");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTags()).contains("beach");
        verify(photoRepository, times(1)).findByTagsContaining("beach");
    }

    @Test
    @DisplayName("Should get photos by multiple tags")
    void testGetPhotosByTags() {
        // Given
        List<String> tags = Arrays.asList("sunset", "beach");
        List<Photo> expectedPhotos = Arrays.asList(testPhoto);
        when(photoRepository.findByAllTags(tags)).thenReturn(expectedPhotos);

        // When
        List<Photo> result = photoService.getPhotosByTags(tags);

        // Then
        assertThat(result).hasSize(1);
        verify(photoRepository, times(1)).findByAllTags(tags);
    }

    @Test
    @DisplayName("Should get photos by location")
    void testGetPhotosByLocation() {
        // Given
        List<Photo> expectedPhotos = Arrays.asList(testPhoto);
        when(photoRepository.findByLocationContainingIgnoreCase("santa monica")).thenReturn(expectedPhotos);

        // When
        List<Photo> result = photoService.getPhotosByLocation("santa monica");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLocation()).containsIgnoringCase("santa monica");
        verify(photoRepository, times(1)).findByLocationContainingIgnoreCase("santa monica");
    }

    @Test
    @DisplayName("Should get only public photos")
    void testGetPublicPhotos() {
        // Given
        List<Photo> expectedPhotos = Arrays.asList(testPhoto);
        when(photoRepository.findByIsPublic(true)).thenReturn(expectedPhotos);

        // When
        List<Photo> result = photoService.getPublicPhotos();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).isPublic()).isTrue();
        verify(photoRepository, times(1)).findByIsPublic(true);
    }

    @Test
    @DisplayName("Should get edited photos")
    void testGetEditedPhotos() {
        // Given
        List<Photo> expectedPhotos = Arrays.asList(testPhoto);
        when(photoRepository.findByIsEdited(true)).thenReturn(expectedPhotos);

        // When
        List<Photo> result = photoService.getEditedPhotos(true);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).isEdited()).isTrue();
        verify(photoRepository, times(1)).findByIsEdited(true);
    }

    @Test
    @DisplayName("Should get photos by date range")
    void testGetPhotosByDateRange() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 8, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 8, 31, 23, 59);
        List<Photo> expectedPhotos = Arrays.asList(testPhoto);
        when(photoRepository.findByCaptureDateBetween(startDate, endDate)).thenReturn(expectedPhotos);

        // When
        List<Photo> result = photoService.getPhotosByDateRange(startDate, endDate);

        // Then
        assertThat(result).hasSize(1);
        verify(photoRepository, times(1)).findByCaptureDateBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should get photos by camera model")
    void testGetPhotosByCameraModel() {
        // Given
        List<Photo> expectedPhotos = Arrays.asList(testPhoto);
        when(photoRepository.findByCameraModel("Canon EOS R5")).thenReturn(expectedPhotos);

        // When
        List<Photo> result = photoService.getPhotosByCameraModel("Canon EOS R5");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCameraSettings().getCameraModel()).isEqualTo("Canon EOS R5");
        verify(photoRepository, times(1)).findByCameraModel("Canon EOS R5");
    }

    @Test
    @DisplayName("Should get recent uploads")
    void testGetRecentUploads() {
        // Given
        List<Photo> expectedPhotos = Arrays.asList(testPhoto);
        when(photoRepository.findTop10ByOrderByUploadDateDesc()).thenReturn(expectedPhotos);

        // When
        List<Photo> result = photoService.getRecentUploads();

        // Then
        assertThat(result).hasSize(1);
        verify(photoRepository, times(1)).findTop10ByOrderByUploadDateDesc();
    }

    @Test
    @DisplayName("Should search photos by title or description")
    void testSearchPhotos() {
        // Given
        List<Photo> expectedPhotos = Arrays.asList(testPhoto);
        when(photoRepository.searchByTitleOrDescription("beautiful")).thenReturn(expectedPhotos);

        // When
        List<Photo> result = photoService.searchPhotos("beautiful");

        // Then
        assertThat(result).hasSize(1);
        verify(photoRepository, times(1)).searchByTitleOrDescription("beautiful");
    }
}
