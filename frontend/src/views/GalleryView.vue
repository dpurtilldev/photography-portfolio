<script setup lang="ts">
import PhotoGallery from '../components/PhotoGallery.vue'
</script>

<template>
  <main>
    <h1>Photo Gallery</h1>
    <PhotoGallery />
  </main>
</template>

<style scoped>
main {
  padding: 2rem;
}

h1 {
  text-align: center;
  margin-bottom: 2rem;
  color: var(--color-heading);
}
</style>
