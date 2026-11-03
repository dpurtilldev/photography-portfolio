package com.portfolio.photographyportfolio.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.PublicAccessType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class AzureBlobStorageService {
    
    @Value("${azure.storage.connection-string}")
    private String connectionString;
    
    @Value("${azure.storage.container-name}")
    private String containerName;
    
    private BlobServiceClient blobServiceClient;
    private BlobContainerClient containerClient;
    
    @PostConstruct
    public void init() {
        try {
            log.info("Initializing Azure Blob Storage Service");
            this.blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            
            this.containerClient = blobServiceClient.getBlobContainerClient(containerName);
            
            // Create container if it doesn't exist
            if (!containerClient.exists()) {
                log.info("Container '{}' does not exist. Creating...", containerName);
                containerClient.create();
                // Set public access for read operations (photos can be publicly accessed)
                containerClient.setAccessPolicy(PublicAccessType.BLOB, null);
                log.info("Container '{}' created successfully", containerName);
            } else {
                log.info("Container '{}' already exists", containerName);
            }
        } catch (Exception e) {
            log.error("Failed to initialize Azure Blob Storage: {}", e.getMessage(), e);
            throw new RuntimeException("Could not initialize Azure Blob Storage", e);
        }
    }
    
    /**
     * Upload a file to Azure Blob Storage
     * @param file The multipart file to upload
     * @param contentType The content type of the file
     * @return The URL of the uploaded blob
     */
    public String uploadFile(MultipartFile file, String contentType) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String blobName = UUID.randomUUID() + extension;
        
        return uploadFile(file.getInputStream(), blobName, contentType, file.getSize());
    }
    
    /**
     * Upload a file to Azure Blob Storage with a specific name
     * @param inputStream The input stream of the file
     * @param blobName The name for the blob
     * @param contentType The content type
     * @param length The length of the file
     * @return The URL of the uploaded blob
     */
    public String uploadFile(InputStream inputStream, String blobName, String contentType, long length) throws IOException {
        try {
            log.info("Uploading blob: {}", blobName);
            
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            
            // Set content type
            BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(contentType);
            
            // Upload the file
            blobClient.upload(inputStream, length, true);
            blobClient.setHttpHeaders(headers);
            
            String blobUrl = blobClient.getBlobUrl();
            log.info("Blob uploaded successfully: {}", blobUrl);
            
            return blobUrl;
        } catch (Exception e) {
            log.error("Failed to upload blob: {}", blobName, e);
            throw new IOException("Failed to upload file to Azure Blob Storage", e);
        }
    }
    
    /**
     * Delete a blob from Azure Blob Storage
     * @param blobUrl The full URL of the blob to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteFile(String blobUrl) {
        try {
            String blobName = extractBlobNameFromUrl(blobUrl);
            log.info("Deleting blob: {}", blobName);
            
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            
            if (blobClient.exists()) {
                blobClient.delete();
                log.info("Blob deleted successfully: {}", blobName);
                return true;
            } else {
                log.warn("Blob does not exist: {}", blobName);
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to delete blob: {}", blobUrl, e);
            return false;
        }
    }
    
    /**
     * Check if a blob exists
     * @param blobUrl The full URL of the blob
     * @return true if exists, false otherwise
     */
    public boolean fileExists(String blobUrl) {
        try {
            String blobName = extractBlobNameFromUrl(blobUrl);
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            return blobClient.exists();
        } catch (Exception e) {
            log.error("Failed to check if blob exists: {}", blobUrl, e);
            return false;
        }
    }
    
    /**
     * Get the input stream for a blob
     * @param blobUrl The full URL of the blob
     * @return InputStream of the blob
     */
    public InputStream downloadFile(String blobUrl) throws IOException {
        try {
            String blobName = extractBlobNameFromUrl(blobUrl);
            log.info("Downloading blob: {}", blobName);
            
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            
            if (!blobClient.exists()) {
                throw new IOException("Blob does not exist: " + blobName);
            }
            
            return blobClient.openInputStream();
        } catch (Exception e) {
            log.error("Failed to download blob: {}", blobUrl, e);
            throw new IOException("Failed to download file from Azure Blob Storage", e);
        }
    }
    
    /**
     * Extract the blob name from the full URL
     * Example: https://mystorageaccount.blob.core.windows.net/photos/abc-123.jpg -> abc-123.jpg
     */
    private String extractBlobNameFromUrl(String blobUrl) {
        if (blobUrl == null || blobUrl.isEmpty()) {
            throw new IllegalArgumentException("Blob URL cannot be null or empty");
        }
        
        // Handle full Azure blob URL
        if (blobUrl.contains(containerName + "/")) {
            int index = blobUrl.indexOf(containerName + "/");
            return blobUrl.substring(index + containerName.length() + 1);
        }
        
        // If it's just the blob name, return as is
        return blobUrl;
    }
    
    /**
     * List all blobs in the container
     */
    public void listAllBlobs() {
        log.info("Listing all blobs in container: {}", containerName);
        for (BlobItem blobItem : containerClient.listBlobs()) {
            log.info("Blob: {}", blobItem.getName());
        }
    }
}
