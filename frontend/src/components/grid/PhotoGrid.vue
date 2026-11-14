<script setup lang="ts">
import type { Photo } from '../../services/photoService'

defineProps<{ photos: Photo[] }>()

function formatDate(value?: string): string {
  if (!value) return ''
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return value
  return date.toLocaleDateString(undefined, { year: 'numeric', month: 'long', day: 'numeric' })
}
</script>

<template>
  <section class="grid" aria-label="Photos">
    <!-- tabindex lets keyboard users (and a tap on phones) reveal the text too -->
    <article v-for="photo in photos" :key="photo.id" class="cover" tabindex="0">
      <img :src="photo.blobUrl" :alt="photo.title" loading="lazy" />

      <div class="details">
        <h2 class="title">{{ photo.title }}</h2>
        <p v-if="photo.description" class="description">{{ photo.description }}</p>
        <p class="date">
          {{ formatDate(photo.captureDate) }}
          <template v-if="photo.location"> · {{ photo.location }}</template>
        </p>
      </div>
    </article>
  </section>
</template>

<style scoped>
/*
 * Two columns with a thin gap. Photos keep their own shape (no cropping),
 * so CSS columns stack them like a masonry wall instead of in fixed rows.
 */
.grid {
  columns: 2;
  column-gap: 0.6rem;
}

.cover {
  position: relative;
  overflow: hidden;
  outline: none;
  margin-bottom: 0.6rem;
  /* Stop a photo from being split across the two columns. */
  break-inside: avoid;
}

.cover img {
  width: 100%;
  height: auto;
  display: block;
}

/* Black layer that fades in to darken the photo on hover. */
.cover::after {
  content: '';
  position: absolute;
  inset: 0;
  background: #000;
  opacity: 0;
  transition: opacity 0.2s ease;
}

/* The text sits top-left and fades in on top of the dark layer. */
.details {
  position: absolute;
  inset: 0;
  z-index: 1;
  padding: 4% 8%;
  overflow: hidden;
  color: #fff;
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  opacity: 0;
  transition: opacity 0.15s ease;
}

.cover:hover::after,
.cover:focus-visible::after,
.cover:focus::after {
  opacity: 0.5;
}

.cover:hover .details,
.cover:focus-visible .details,
.cover:focus .details {
  opacity: 1;
}

.title {
  font-size: 22px;
  line-height: 25px;
  font-weight: 400;
  color: #fff;
  padding-bottom: 11px;
}

.description {
  font-size: 14px;
  line-height: 20px;
  padding-bottom: 11px;
  white-space: pre-line;
  max-width: 34rem;
}

.date {
  font-size: 13px;
  line-height: 17px;
  font-weight: 600;
}

/* Phones: one photo per row. */
@media (max-width: 540px) {
  .grid {
    columns: 1;
  }

  .cover {
    margin-bottom: 1rem;
  }

  .title {
    font-size: 19px;
    line-height: 22px;
  }
}
</style>
