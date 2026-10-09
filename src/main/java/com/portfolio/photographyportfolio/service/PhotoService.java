package com.portfolio.photographyportfolio.service;

import com.portfolio.photographyportfolio.model.Photo;
import com.portfolio.photographyportfolio.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoService {
    
    private final PhotoRepository photoRepository;

    
    
    /**
     * Create a new photo entry
     */
    @Transactional
    public Photo createPhoto(Photo photo) {
        log.info("Creating new photo: {}", photo.getTitle());
        photo.setUploadDate(LocalDateTime.now());
        if (photo.getLastEditedDate() == null && photo.isEdited()) {
            photo.setLastEditedDate(LocalDateTime.now());
        }
        return photoRepository.save(photo);
    }
    
    /**
     * Get all photos
     */
    public List<Photo> getAllPhotos() {
        log.info("Fetching all photos");
        return photoRepository.findAll();
    }
    
    /**
     * Get photo by ID
     */
    public Optional<Photo> getPhotoById(String id) {
        log.info("Fetching photo with id: {}", id);
        return photoRepository.findById(id);
    }
    
    /**
     * Update existing photo
     */
    @Transactional
    public Photo updatePhoto(String id, Photo photoDetails) {
        log.info("Updating photo with id: {}", id);
        return photoRepository.findById(id)
                .map(existingPhoto -> {
                    existingPhoto.setTitle(photoDetails.getTitle());
                    existingPhoto.setDescription(photoDetails.getDescription());
                    existingPhoto.setFileName(photoDetails.getFileName());
                    existingPhoto.setFilePath(photoDetails.getFilePath());
                    existingPhoto.setFileType(photoDetails.getFileType());
                    existingPhoto.setFileSize(photoDetails.getFileSize());
                    existingPhoto.setCaptureDate(photoDetails.getCaptureDate());
                    existingPhoto.setLastEditedDate(LocalDateTime.now());
                    existingPhoto.setCameraSettings(photoDetails.getCameraSettings());
                    existingPhoto.setLocation(photoDetails.getLocation());
                    existingPhoto.setTags(photoDetails.getTags());
                    existingPhoto.setEditingSoftware(photoDetails.getEditingSoftware());
                    existingPhoto.setEdited(photoDetails.isEdited());
                    existingPhoto.setThumbnailPath(photoDetails.getThumbnailPath());
                    existingPhoto.setPublic(photoDetails.isPublic());
                    return photoRepository.save(existingPhoto);
                })
                .orElseThrow(() -> new RuntimeException("Photo not found with id: " + id));
    }
    
    /**
     * Delete photo by ID
     */
    @Transactional
    public void deletePhoto(String id) {
        log.info("Deleting photo with id: {}", id);
        photoRepository.deleteById(id);
    }
    
    /**
     * Search photos by title
     */
    public List<Photo> searchByTitle(String title) {
        log.info("Searching photos by title: {}", title);
        return photoRepository.findByTitleContainingIgnoreCase(title);
    }
    
    /**
     * Get photos by tag
     */
    public List<Photo> getPhotosByTag(String tag) {
        log.info("Fetching photos with tag: {}", tag);
        return photoRepository.findByTagsContaining(tag);
    }
    
    /**
     * Get photos by location
     */
    public List<Photo> getPhotosByLocation(String location) {
        log.info("Fetching photos from location: {}", location);
        return photoRepository.findByLocationContainingIgnoreCase(location);
    }
    
    /**
     * Get only public photos
     */
    public List<Photo> getPublicPhotos() {
        log.info("Fetching public photos");
        return photoRepository.findByIsPublic(true);
    }
    
    /**
     * Get photos by edited status
     */
    public List<Photo> getEditedPhotos(boolean isEdited) {
        log.info("Fetching {} photos", isEdited ? "edited" : "original");
        return photoRepository.findByIsEdited(isEdited);
    }
    
    /**
     * Get photos by date range
     */
    public List<Photo> getPhotosByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching photos between {} and {}", startDate, endDate);
        return photoRepository.findByCaptureDateBetween(startDate, endDate);
    }
    
    /**
     * Get photos by camera model
     */
    public List<Photo> getPhotosByCameraModel(String cameraModel) {
        log.info("Fetching photos taken with: {}", cameraModel);
        return photoRepository.findByCameraModel(cameraModel);
    }
    
    /**
     * Get recent uploads
     */
    public List<Photo> getRecentUploads() {
        log.info("Fetching recent uploads");
        return photoRepository.findTop10ByOrderByUploadDateDesc();
    }
    
    /**
     * Search photos by title or description
     */
    public List<Photo> searchPhotos(String searchTerm) {
        log.info("Searching photos with term: {}", searchTerm);
        return photoRepository.searchByTitleOrDescription(searchTerm);
    }
    
    /**
     * Get photos by multiple tags
     */
    public List<Photo> getPhotosByTags(List<String> tags) {
        log.info("Fetching photos with all tags: {}", tags);
        return photoRepository.findByAllTags(tags);
    }
}
