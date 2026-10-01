package com.portfolio.photographyportfolio.repository;

import com.portfolio.photographyportfolio.model.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhotoRepository extends MongoRepository<Photo, String> {
    
    // Find photos by title (case-insensitive)
    List<Photo> findByTitleContainingIgnoreCase(String title);
    
    // Find photos by tag
    List<Photo> findByTagsContaining(String tag);
    
    // Find photos by location
    List<Photo> findByLocationContainingIgnoreCase(String location);
    
    // Find public photos only
    List<Photo> findByIsPublic(boolean isPublic);
    
    // Find edited photos
    List<Photo> findByIsEdited(boolean isEdited);
    
    // Find photos by date range
    List<Photo> findByCaptureDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find photos by camera model
    @Query("{ 'cameraSettings.cameraModel' : ?0 }")
    List<Photo> findByCameraModel(String cameraModel);
    
    // Find photos by editing software
    List<Photo> findByEditingSoftware(String editingSoftware);
    
    // Find recent uploads
    List<Photo> findTop10ByOrderByUploadDateDesc();
    
    // Find photos by multiple tags
    @Query("{ 'tags' : { $all : ?0 } }")
    List<Photo> findByAllTags(List<String> tags);
    
    // Search photos by title or description
    @Query("{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?0, $options: 'i' } } ] }")
    List<Photo> searchByTitleOrDescription(String searchTerm);
}
