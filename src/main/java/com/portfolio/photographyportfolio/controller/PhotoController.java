package com.portfolio.photographyportfolio.controller;

import com.portfolio.photographyportfolio.model.Photo;
import com.portfolio.photographyportfolio.service.PhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Configure appropriately for production
public class PhotoController {
    
    private final PhotoService photoService;
    
    /**
     * Create a new photo
     * POST /api/photos
     */
    @PostMapping
    public ResponseEntity<Photo> createPhoto(@Valid @RequestBody Photo photo) {
        log.info("REST request to create photo: {}", photo.getTitle());
        Photo createdPhoto = photoService.createPhoto(photo);
        return new ResponseEntity<>(createdPhoto, HttpStatus.CREATED);
    }
    
    /**
     * Get all photos
     * GET /api/photos
     */
    @GetMapping
    public ResponseEntity<List<Photo>> getAllPhotos() {
        log.info("REST request to get all photos");
        List<Photo> photos = photoService.getAllPhotos();
        return ResponseEntity.ok(photos);
    }
    
    /**
     * Get photo by ID
     * GET /api/photos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Photo> getPhotoById(@PathVariable String id) {
        log.info("REST request to get photo: {}", id);
        return photoService.getPhotoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Update photo
     * PUT /api/photos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Photo> updatePhoto(@PathVariable String id, 
                                            @Valid @RequestBody Photo photoDetails) {
        log.info("REST request to update photo: {}", id);
        try {
            Photo updatedPhoto = photoService.updatePhoto(id, photoDetails);
            return ResponseEntity.ok(updatedPhoto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete photo
     * DELETE /api/photos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable String id) {
        log.info("REST request to delete photo: {}", id);
        photoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Search photos by title
     * GET /api/photos/search/title?q=query
     */
    @GetMapping("/search/title")
    public ResponseEntity<List<Photo>> searchByTitle(@RequestParam String q) {
        log.info("REST request to search photos by title: {}", q);
        List<Photo> photos = photoService.searchByTitle(q);
        return ResponseEntity.ok(photos);
    }
    
    /**
     * Search photos by title or description
     * GET /api/photos/search?q=query
     */
    @GetMapping("/search")
    public ResponseEntity<List<Photo>> searchPhotos(@RequestParam String q) {
        log.info("REST request to search photos: {}", q);
        List<Photo> photos = photoService.searchPhotos(q);
        return ResponseEntity.ok(photos);
    }
    
    /**
     * Get photos by tag
     * GET /api/photos/tag/{tag}
     */
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<Photo>> getPhotosByTag(@PathVariable String tag) {
        log.info("REST request to get photos by tag: {}", tag);
        List<Photo> photos = photoService.getPhotosByTag(tag);
        return ResponseEntity.ok(photos);
    }
    
    /**
     * Get photos by multiple tags
     * POST /api/photos/tags
     */
    @PostMapping("/tags")
    public ResponseEntity<List<Photo>> getPhotosByTags(@RequestBody List<String> tags) {
        log.info("REST request to get photos by tags: {}", tags);
        List<Photo> photos = photoService.getPhotosByTags(tags);
        return ResponseEntity.ok(photos);
    }
    
    /**
     * Get photos by location
     * GET /api/photos/location/{location}
     */
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Photo>> getPhotosByLocation(@PathVariable String location) {
        log.info("REST request to get photos by location: {}", location);
        List<Photo> photos = photoService.getPhotosByLocation(location);
        return ResponseEntity.ok(photos);
    }
    
    /**
     * Get public photos only
     * GET /api/photos/public
     */
    @GetMapping("/public")
    public ResponseEntity<List<Photo>> getPublicPhotos() {
        log.info("REST request to get public photos");
        List<Photo> photos = photoService.getPublicPhotos();
        return ResponseEntity.ok(photos);
    }
    
    /**
     * Get edited photos
     * GET /api/photos/edited?isEdited=true
     */
    @GetMapping("/edited")
    public ResponseEntity<List<Photo>> getEditedPhotos(@RequestParam(defaultValue = "true") boolean isEdited) {
        log.info("REST request to get edited photos: {}", isEdited);
        List<Photo> photos = photoService.getEditedPhotos(isEdited);
        return ResponseEntity.ok(photos);
    }
    
    /**
     * Get photos by date range
     * GET /api/photos/date-range?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Photo>> getPhotosByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        log.info("REST request to get photos by date range: {} to {}", start, end);
        List<Photo> photos = photoService.getPhotosByDateRange(start, end);
        return ResponseEntity.ok(photos);
    }
    
    /**
     * Get photos by camera model
     * GET /api/photos/camera/{model}
     */
    @GetMapping("/camera/{model}")
    public ResponseEntity<List<Photo>> getPhotosByCameraModel(@PathVariable String model) {
        log.info("REST request to get photos by camera model: {}", model);
        List<Photo> photos = photoService.getPhotosByCameraModel(model);
        return ResponseEntity.ok(photos);
    }
    
    /**
     * Get recent uploads
     * GET /api/photos/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Photo>> getRecentUploads() {
        log.info("REST request to get recent uploads");
        List<Photo> photos = photoService.getRecentUploads();
        return ResponseEntity.ok(photos);
    }
    
    /**
     * Health check endpoint
     * GET /api/photos/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Photography Portfolio API is running!");
    }
}
