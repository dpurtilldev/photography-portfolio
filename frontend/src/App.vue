<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="app-container">
    <header>
      <nav>
        <RouterLink to="/" class="nav-button">Home</RouterLink>
        <RouterLink to="/gallery" class="nav-button">Gallery</RouterLink>
        <RouterLink to="/userpage" class="nav-button">Userpage</RouterLink>
        <RouterLink to="/about" class="nav-button">About</RouterLink>
      </nav>
    </header>

    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

header {
  background-color: var(--color-background-soft);
  padding: 1rem 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 100%;
}

nav {
  display: flex;
  gap: 1rem;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
}

.main-content {
  flex: 1;
  width: 100%;
  display: block;
}

.nav-button {
  padding: 0.75rem 1.5rem;
  text-decoration: none;
  color: var(--color-text);
  background-color: var(--color-background);
  border: 1px solid var(--color-border);
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.nav-button:hover {
  background-color: var(--color-background-mute);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.nav-button.router-link-active {
  background-color: var(--color-background-mute);
  color: var(--vt-c-indigo);
  border-color: var(--vt-c-indigo);
}

@media (max-width: 768px) {
  nav {
    gap: 0.5rem;
  }
  
  .nav-button {
    padding: 0.5rem 1rem;
    font-size: 0.9rem;
  }
}
</style>
