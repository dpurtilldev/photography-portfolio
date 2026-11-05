<script setup lang="ts">
</script>

<template>
  <main>
    <div class="user-page">
      <h1>User Page</h1>
      <div class="user-content">
        <p>Welcome to your user page!</p>
        <p>This page will contain user-specific information and settings.</p>
      </div>
    </div>
  </main>
</template>

<style scoped>
main {
  padding: 2rem;
}

.user-page {
  max-width: 800px;
  margin: 0 auto;
}

h1 {
  text-align: center;
  margin-bottom: 2rem;
  color: var(--color-heading);
}

.user-content {
  background-color: var(--color-background-soft);
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-content p {
  margin-bottom: 1rem;
  line-height: 1.6;
}
</style>
