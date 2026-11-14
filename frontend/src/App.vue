<script setup lang="ts">
import { RouterView } from 'vue-router'
import SideMenu from './components/SideMenu.vue'
</script>

<template>
  <div class="app-container">
    <SideMenu />

    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.app-container {
  min-height: 100vh;
}

.main-content {
  width: 100%;
  /* Leave room for the burger button in the top-left corner. */
  padding-top: 4rem;
}
</style>
