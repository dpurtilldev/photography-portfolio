<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { fetchPhotos, type Photo } from '../services/photoService';



const photos = ref<Photo[]>([]);
const loading = ref(true);
const error = ref<string | null>(null);

onMounted(async () => {
    try {
        photos.value = await fetchPhotos();
    } catch (e) {
        error.value = "Failed to load your Photos";
    } finally {
        loading.value = false;
    }
});

</script>

<template>
    <div class="gallery">
        <p v-if="loading" class="loading-message">Loading photos...</p>
        <p v-else-if="error" class="error">{{ error }}</p>
        
        <div v-else class="photo-list">
            <div v-for="photo in photos" :key="photo.id" class="photo-item">
                <img :src="photo.blobUrl" :alt="photo.title" />
                <div class="photo-info">
                    <h3>{{ photo.title }}</h3>
                    <p>Descritption: {{ photo.description }}</p>
                    <p>Date posted: {{ photo.captureDate }}</p>
                    <p>Camera Model: {{ photo.cameraSettings?.cameraModel }}</p>
                    <p>Lens Model: {{ photo.cameraSettings?.lens }}</p>
                    <p>Aperture: {{ photo.cameraSettings?.aperture }}</p>
                    <p>Shutterspeed: {{ photo.cameraSettings?.shutterSpeed }}</p>
                    <p>Focal Length: {{ photo.cameraSettings?.focalLength }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.gallery {
    padding: 2rem;
    max-width: 1200px;
    margin: 0 auto;
}

.loading-message {
    text-align: center;
    font-size: 1.2rem;
    color: var(--color-text);
}

.error {
    color: #dc3545;
    font-weight: bold;
    text-align: center;
    font-size: 1.2rem;
}

.photo-list {
    display: flex;
    flex-direction: column;
    gap: 3rem; /* Spacing between each photo item */
}

.photo-item {
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 2rem; /* Spacing between image and text */
    padding: 1.5rem;
    background-color: var(--color-background-soft);
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.photo-item:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.photo-item img {
    width: 600px;
    height: px;
    object-fit: cover;
    border-radius: 8px;
    flex-shrink: 0; /* Prevent image from shrinking */
}

.photo-info {
    width: 500px; /* Take up remaining space */
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
}

.photo-info h3 {
    margin: 0;
    font-size: 1.5rem;
    color: var(--color-heading);
}

.photo-info p {
    margin: 0;
    line-height: 1.6;
    color: var(--color-text);
}

/* Responsive design for smaller screens */
@media (max-width: 768px) {
    .photo-item {
        flex-direction: column;
        gap: 1rem;
    }
    
    .photo-item img {
        width: 100%;
        height: auto;
        max-height: 300px;
    }
}
</style>