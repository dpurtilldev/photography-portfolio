package com.portfolio.photographyportfolio.repository;

import com.portfolio.photographyportfolio.config.TestMongoConfig;
import com.portfolio.photographyportfolio.model.Photo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(TestMongoConfig.class)
@DisplayName("PhotoRepository MongoDB Integration Tests")
class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository photoRepository;

    private Photo photo1;
    private Photo photo2;
    private Photo photo3;

    @BeforeEach
    void setUp() {
        photoRepository.deleteAll();

        Photo.CameraSettings camera1 = Photo.CameraSettings.builder()
                .cameraModel("Canon EOS R5")
                .lens("RF 24-70mm F2.8")
                .aperture("f/8")
                .shutterSpeed("1/250")
                .iso(100)
                .focalLength("35mm")
                .build();

        Photo.CameraSettings camera2 = Photo.CameraSettings.builder()
                .cameraModel("Sony A7 IV")
                .lens("FE 70-200mm F2.8")
                .aperture("f/5.6")
                .shutterSpeed("1/500")
                .iso(200)
                .focalLength("200mm")
                .build();

        photo1 = Photo.builder()
                .title("Sunset Beach")
                .description("Beautiful sunset at the beach")
                .fileName("sunset.jpg")
                .filePath("/photos/sunset.jpg")
                .fileType("image/jpeg")
                .fileSize(2048576L)
                .captureDate(LocalDateTime.of(2024, 8, 15, 18, 30))
                .uploadDate(LocalDateTime.of(2024, 8, 15, 19, 0))
                .location("Santa Monica Beach, California")
                .tags(Arrays.asList("sunset", "beach", "landscape"))
                .isEdited(true)
                .editingSoftware("Adobe Lightroom")
                .isPublic(true)
                .cameraSettings(camera1)
                .build();

        photo2 = Photo.builder()
                .title("Mountain Peak")
                .description("Snow-covered mountain peak")
                .fileName("mountain.jpg")
                .filePath("/photos/mountain.jpg")
                .fileType("image/jpeg")
                .fileSize(3048576L)
                .captureDate(LocalDateTime.of(2024, 7, 10, 12, 0))
                .uploadDate(LocalDateTime.of(2024, 7, 10, 13, 0))
                .location("Rocky Mountains, Colorado")
                .tags(Arrays.asList("mountain", "snow", "landscape"))
                .isEdited(false)
                .isPublic(true)
                .cameraSettings(camera2)
                .build();

        photo3 = Photo.builder()
                .title("City Lights")
                .description("Urban nightscape with city lights")
                .fileName("city.jpg")
                .filePath("/photos/city.jpg")
                .fileType("image/jpeg")
                .fileSize(2548576L)
                .captureDate(LocalDateTime.of(2024, 9, 5, 20, 45))
                .uploadDate(LocalDateTime.of(2024, 9, 5, 21, 0))
                .location("New York City")
                .tags(Arrays.asList("city", "night", "urban"))
                .isEdited(true)
                .editingSoftware("Photoshop")
                .isPublic(false)
                .cameraSettings(camera1)
                .build();

        photoRepository.saveAll(Arrays.asList(photo1, photo2, photo3));
    }

    @Test
    @DisplayName("Should save and retrieve photo")
    void testSaveAndFindPhoto() {
        // Given
        Photo newPhoto = Photo.builder()
                .title("Test Photo")
                .fileName("test.jpg")
                .filePath("/photos/test.jpg")
                .captureDate(LocalDateTime.now())
                .build();

        // When
        Photo savedPhoto = photoRepository.save(newPhoto);

        // Then
        assertThat(savedPhoto.getId()).isNotNull();
        
        Optional<Photo> retrieved = photoRepository.findById(savedPhoto.getId());
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getTitle()).isEqualTo("Test Photo");
    }

    @Test
    @DisplayName("Should find all photos")
    void testFindAll() {
        // When
        List<Photo> photos = photoRepository.findAll();

        // Then
        assertThat(photos).hasSize(3);
    }

    @Test
    @DisplayName("Should find photos by title containing (case insensitive)")
    void testFindByTitleContainingIgnoreCase() {
        // When
        List<Photo> photos = photoRepository.findByTitleContainingIgnoreCase("sunset");

        // Then
        assertThat(photos).hasSize(1);
        assertThat(photos.get(0).getTitle()).isEqualTo("Sunset Beach");

        // Test case insensitive
        List<Photo> photosUpperCase = photoRepository.findByTitleContainingIgnoreCase("MOUNTAIN");
        assertThat(photosUpperCase).hasSize(1);
        assertThat(photosUpperCase.get(0).getTitle()).isEqualTo("Mountain Peak");
    }

    @Test
    @DisplayName("Should find photos by tag")
    void testFindByTagsContaining() {
        // When
        List<Photo> beachPhotos = photoRepository.findByTagsContaining("beach");
        List<Photo> landscapePhotos = photoRepository.findByTagsContaining("landscape");

        // Then
        assertThat(beachPhotos).hasSize(1);
        assertThat(beachPhotos.get(0).getTitle()).isEqualTo("Sunset Beach");

        assertThat(landscapePhotos).hasSize(2);
    }

    @Test
    @DisplayName("Should find photos by all tags")
    void testFindByAllTags() {
        // When
        List<String> tags = Arrays.asList("sunset", "beach");
        List<Photo> photos = photoRepository.findByAllTags(tags);

        // Then
        assertThat(photos).hasSize(1);
        assertThat(photos.get(0).getTitle()).isEqualTo("Sunset Beach");
    }

    @Test
    @DisplayName("Should find photos by location containing (case insensitive)")
    void testFindByLocationContainingIgnoreCase() {
        // When
        List<Photo> californiaPhotos = photoRepository.findByLocationContainingIgnoreCase("california");
        List<Photo> mountainPhotos = photoRepository.findByLocationContainingIgnoreCase("mountains");

        // Then
        assertThat(californiaPhotos).hasSize(1);
        assertThat(californiaPhotos.get(0).getLocation()).contains("California");

        assertThat(mountainPhotos).hasSize(1);
        assertThat(mountainPhotos.get(0).getLocation()).contains("Mountains");
    }

    @Test
    @DisplayName("Should find public photos")
    void testFindByIsPublic() {
        // When
        List<Photo> publicPhotos = photoRepository.findByIsPublic(true);
        List<Photo> privatePhotos = photoRepository.findByIsPublic(false);

        // Then
        assertThat(publicPhotos).hasSize(2);
        assertThat(privatePhotos).hasSize(1);
        assertThat(privatePhotos.get(0).getTitle()).isEqualTo("City Lights");
    }

    @Test
    @DisplayName("Should find edited photos")
    void testFindByIsEdited() {
        // When
        List<Photo> editedPhotos = photoRepository.findByIsEdited(true);
        List<Photo> originalPhotos = photoRepository.findByIsEdited(false);

        // Then
        assertThat(editedPhotos).hasSize(2);
        assertThat(originalPhotos).hasSize(1);
        assertThat(originalPhotos.get(0).getTitle()).isEqualTo("Mountain Peak");
    }

    @Test
    @DisplayName("Should find photos by capture date range")
    void testFindByCaptureDateBetween() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 8, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 8, 31, 23, 59);

        // When
        List<Photo> photos = photoRepository.findByCaptureDateBetween(startDate, endDate);

        // Then
        assertThat(photos).hasSize(1);
        assertThat(photos.get(0).getTitle()).isEqualTo("Sunset Beach");
    }

    @Test
    @DisplayName("Should find photos by camera model")
    void testFindByCameraModel() {
        // When
        List<Photo> canonPhotos = photoRepository.findByCameraModel("Canon EOS R5");
        List<Photo> sonyPhotos = photoRepository.findByCameraModel("Sony A7 IV");

        // Then
        assertThat(canonPhotos).hasSize(2);
        assertThat(sonyPhotos).hasSize(1);
        assertThat(sonyPhotos.get(0).getTitle()).isEqualTo("Mountain Peak");
    }

    @Test
    @DisplayName("Should find photos by editing software")
    void testFindByEditingSoftware() {
        // When
        List<Photo> lightroomPhotos = photoRepository.findByEditingSoftware("Adobe Lightroom");
        List<Photo> photoshopPhotos = photoRepository.findByEditingSoftware("Photoshop");

        // Then
        assertThat(lightroomPhotos).hasSize(1);
        assertThat(lightroomPhotos.get(0).getTitle()).isEqualTo("Sunset Beach");

        assertThat(photoshopPhotos).hasSize(1);
        assertThat(photoshopPhotos.get(0).getTitle()).isEqualTo("City Lights");
    }

    @Test
    @DisplayName("Should find top 10 recent uploads ordered by upload date desc")
    void testFindTop10ByOrderByUploadDateDesc() {
        // When
        List<Photo> recentPhotos = photoRepository.findTop10ByOrderByUploadDateDesc();

        // Then
        assertThat(recentPhotos).hasSize(3); // We only have 3 photos
        // Most recent should be first
        assertThat(recentPhotos.get(0).getTitle()).isEqualTo("City Lights");
        assertThat(recentPhotos.get(1).getTitle()).isEqualTo("Sunset Beach");
        assertThat(recentPhotos.get(2).getTitle()).isEqualTo("Mountain Peak");
    }

    @Test
    @DisplayName("Should search photos by title or description")
    void testSearchByTitleOrDescription() {
        // When
        List<Photo> sunsetResults = photoRepository.searchByTitleOrDescription("sunset");
        List<Photo> urbanResults = photoRepository.searchByTitleOrDescription("urban");
        List<Photo> snowResults = photoRepository.searchByTitleOrDescription("snow");

        // Then
        assertThat(sunsetResults).hasSize(1);
        assertThat(sunsetResults.get(0).getTitle()).isEqualTo("Sunset Beach");

        assertThat(urbanResults).hasSize(1);
        assertThat(urbanResults.get(0).getDescription()).contains("Urban");

        assertThat(snowResults).hasSize(1);
        assertThat(snowResults.get(0).getDescription()).contains("Snow");
    }

    @Test
    @DisplayName("Should delete photo by ID")
    void testDeleteById() {
        // Given
        List<Photo> allPhotos = photoRepository.findAll();
        String photoId = allPhotos.get(0).getId();

        // When
        photoRepository.deleteById(photoId);

        // Then
        Optional<Photo> deletedPhoto = photoRepository.findById(photoId);
        assertThat(deletedPhoto).isEmpty();
        assertThat(photoRepository.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("Should update photo")
    void testUpdatePhoto() {
        // Given
        List<Photo> photos = photoRepository.findByTitleContainingIgnoreCase("sunset");
        Photo photo = photos.get(0);
        String photoId = photo.getId();

        // When
        photo.setTitle("Updated Sunset");
        photo.setDescription("Updated description");
        photo.setPublic(false);
        photoRepository.save(photo);

        // Then
        Optional<Photo> updatedPhoto = photoRepository.findById(photoId);
        assertThat(updatedPhoto).isPresent();
        assertThat(updatedPhoto.get().getTitle()).isEqualTo("Updated Sunset");
        assertThat(updatedPhoto.get().getDescription()).isEqualTo("Updated description");
        assertThat(updatedPhoto.get().isPublic()).isFalse();
    }

    @Test
    @DisplayName("Should handle empty results gracefully")
    void testEmptyResults() {
        // When
        List<Photo> nonExistentTag = photoRepository.findByTagsContaining("nonexistent");
        List<Photo> nonExistentLocation = photoRepository.findByLocationContainingIgnoreCase("Mars");
        List<Photo> nonExistentCamera = photoRepository.findByCameraModel("iPhone");

        // Then
        assertThat(nonExistentTag).isEmpty();
        assertThat(nonExistentLocation).isEmpty();
        assertThat(nonExistentCamera).isEmpty();
    }

    @Test
    @DisplayName("Should persist nested camera settings")
    void testNestedCameraSettings() {
        // When
        List<Photo> photos = photoRepository.findByTitleContainingIgnoreCase("sunset");
        Photo photo = photos.get(0);

        // Then
        assertThat(photo.getCameraSettings()).isNotNull();
        assertThat(photo.getCameraSettings().getCameraModel()).isEqualTo("Canon EOS R5");
        assertThat(photo.getCameraSettings().getLens()).isEqualTo("RF 24-70mm F2.8");
        assertThat(photo.getCameraSettings().getAperture()).isEqualTo("f/8");
        assertThat(photo.getCameraSettings().getShutterSpeed()).isEqualTo("1/250");
        assertThat(photo.getCameraSettings().getIso()).isEqualTo(100);
        assertThat(photo.getCameraSettings().getFocalLength()).isEqualTo("35mm");
    }

    @Test
    @DisplayName("Should persist tags as list")
    void testTagsList() {
        // When
        List<Photo> photos = photoRepository.findByTitleContainingIgnoreCase("mountain");
        Photo photo = photos.get(0);

        // Then
        assertThat(photo.getTags()).isNotNull();
        assertThat(photo.getTags()).hasSize(3);
        assertThat(photo.getTags()).containsExactlyInAnyOrder("mountain", "snow", "landscape");
    }
}
