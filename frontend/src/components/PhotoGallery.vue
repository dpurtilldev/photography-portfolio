<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { fetchPhotos, type Photo } from '../services/photoService';



const photos = ref<Photo[]>([]);
const loading = ref(true);
const error = ref<string | null>(null);

// const getImageUrl = (photo: Photo) => {
//     // Construct the image URL from the filePath or fileName
//     return `http://localhost:8181/${photo.filePath}` || `http://localhost:8181/uploads/photos/${photo.fileName}`;
// };


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
    <!-- <div class="photo-gallery">
        <h1>Photo Gallery</h1>
        
        <p v-if="loading">Loading...</p>
        <p v-else-if="error" class="error">{{ error }}</p>
        
        <div v-else class="gallery-grid">
            <div v-for="photo in photos" :key="photo.id" class="photo-card">
                <img :src="getImageUrl(photo)" :alt="photo.title" />
                <div class="photo-details">
                    <h3>{{ photo.title }}</h3>
                    <p v-if="photo.description" class="description">{{ photo.description }}</p>
                    <div v-if="photo.tags && photo.tags.length > 0" class="tags">
                        <span v-for="tag in photo.tags" :key="tag" class="tag">{{ tag }}</span>
                    </div>
                    <p v-if="photo.location" class="location">📍 {{ photo.location }}</p>
                    <p v-if="photo.uploadDate" class="upload-date">
                        Uploaded: {{ new Date(photo.uploadDate).toLocaleDateString() }}
                    </p>
                    <div v-if="photo.cameraSettings" class="camera-info">
                        <p v-if="photo.cameraSettings.cameraModel">📷 {{ photo.cameraSettings.cameraModel }}</p>
                        <p v-if="photo.cameraSettings.lens">{{ photo.cameraSettings.lens }}</p>
                        <p v-if="photo.cameraSettings.aperture">{{ photo.cameraSettings.aperture }}</p>
                        <p v-if="photo.cameraSettings.iso">{{ photo.cameraSettings.iso }}</p>
                        <p v-if="photo.cameraSettings.focalLength">{{ photo.cameraSettings.focalLength }}</p>
                    </div>
                </div>
            </div>
        </div>
    </div> -->

    <div class="gallery">
        <div v-for="photo in photos" :key="photo.id" class="photo-item">
            <img :src="photo.blobUrl" :alt="photo.title" />
            <div class="photo-info">

                <h3>{{ photo.title }}</h3>
                <p>{{ photo.description }}</p>


            </div>
        </div>

    </div>
</template>

<style scoped>
/* .photo-gallery {
    padding: 20px;
}





.photo-card {
    border: 1px solid #ddd;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    transition: transform 0.2s;
}

.photo-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 8px rgba(0,0,0,0.2);
}

.photo-card img {
    width: 100%;
    height: 250px;
    object-fit: cover;
}

.photo-details {
    padding: 15px;
}

.photo-details h3 {
    margin: 0 0 10px 0;
    font-size: 1.2em;
}

.description {
    color: #666;
    margin: 10px 0;
}

.category {
    display: inline-block;
    background-color: #007bff;
    color: white;
    padding: 5px 10px;
    border-radius: 4px;
    font-size: 0.85em;
    margin-top: 10px;
}

.tags {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
    margin-top: 10px;
}

.tag {
    display: inline-block;
    background-color: #007bff;
    color: white;
    padding: 5px 10px;
    border-radius: 4px;
    font-size: 0.85em;
}

.location {
    font-size: 0.9em;
    color: #555;
    margin-top: 8px;
}

.camera-info {
    font-size: 0.85em;
    color: #777;
    margin-top: 8px;
}

.upload-date {
    font-size: 0.85em;
    color: #999;
    margin-top: 10px;
} */

.gallery {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
    padding: 20px;
}

.photo-item img {
    width: 100%;
    height: 300px;
    /* Fixed height is key! */
    object-fit: cover;
    border-radius: 8px;
}

.photo-item {
  display: flex;
  flex-direction: column;
}

.error {
    color: red;
    font-weight: bold;
}

</style>