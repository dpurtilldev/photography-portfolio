<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { fetchPhotos, type Photo } from '../services/photoService';
import PhotoGrid from './grid/PhotoGrid.vue';

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
        <p v-if="loading" class="message">Loading photos...</p>
        <p v-else-if="error" class="message error">{{ error }}</p>
        <p v-else-if="photos.length === 0" class="message">No photos yet.</p>
        <PhotoGrid v-else :photos="photos" />
    </div>
</template>

<style scoped>
.gallery {
    max-width: 1600px;
    margin: 0 auto;
}

.message {
    text-align: center;
    font-size: 1.2rem;
    color: var(--color-text);
    padding: 4rem 0;
}

.error {
    color: #dc3545;
    font-weight: bold;
}
</style>
