<script setup lang="ts">
import { onMounted, onUnmounted, ref, watch } from 'vue'
import { RouterLink, useRoute } from 'vue-router'

const links = [
  { to: '/', label: 'Home' },
  { to: '/userpage', label: 'Userpage' },
  { to: '/about', label: 'About' },
]

const open = ref(false)
const menuRoot = ref<HTMLElement | null>(null)
const route = useRoute()

// Close the menu after navigating to a new page.
watch(
  () => route.fullPath,
  () => {
    open.value = false
  },
)

// Close when clicking anywhere outside the menu.
function onDocumentClick(e: MouseEvent) {
  if (open.value && menuRoot.value && !menuRoot.value.contains(e.target as Node)) {
    open.value = false
  }
}

function onKeydown(e: KeyboardEvent) {
  if (e.key === 'Escape') open.value = false
}

onMounted(() => {
  document.addEventListener('click', onDocumentClick)
  document.addEventListener('keydown', onKeydown)
})

onUnmounted(() => {
  document.removeEventListener('click', onDocumentClick)
  document.removeEventListener('keydown', onKeydown)
})
</script>

<template>
  <div ref="menuRoot" class="side-menu" :class="{ open }">
    <button
      type="button"
      class="burger"
      :aria-expanded="open"
      aria-controls="side-menu-links"
      :aria-label="open ? 'Close menu' : 'Open menu'"
      @click="open = !open"
    >
      <span class="bar" />
      <span class="bar" />
      <span class="bar" />
    </button>

    <nav id="side-menu-links" class="links" aria-label="Main">
      <RouterLink
        v-for="(link, i) in links"
        :key="link.to"
        :to="link.to"
        class="link"
        :style="{ '--i': i }"
      >
        {{ link.label }}
      </RouterLink>
    </nav>
  </div>
</template>

<style scoped>
.side-menu {
  position: fixed;
  top: 1rem;
  left: 1rem;
  z-index: 100;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

/* ---------- Burger button ---------- */
.burger {
  width: 48px;
  height: 48px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 6px;
  border: 1px solid var(--color-border-hover);
  border-radius: 12px;
  background: var(--color-background-soft);
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
  transition: background-color 0.2s;
}

.burger:hover {
  background: var(--color-background-mute);
}

.bar {
  display: block;
  width: 22px;
  height: 2px;
  border-radius: 2px;
  background: var(--color-heading);
  transition:
    transform 0.3s ease,
    opacity 0.2s ease;
}

/* Turn the three bars into an X when open. */
.open .bar:nth-child(1) {
  transform: translateY(8px) rotate(45deg);
}

.open .bar:nth-child(2) {
  opacity: 0;
}

.open .bar:nth-child(3) {
  transform: translateY(-8px) rotate(-45deg);
}

/* ---------- Links that drop down ---------- */
.links {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-top: 0.75rem;
  /* Hidden links can't be tabbed to. The delay lets them animate out first. */
  visibility: hidden;
  transition: visibility 0s linear 0.4s;
}

.open .links {
  visibility: visible;
  transition-delay: 0s;
}

.link {
  min-width: 10rem;
  padding: 0.65rem 1.1rem;
  border-radius: 10px;
  border: 1px solid var(--color-border);
  background: var(--color-background-soft);
  color: var(--color-heading);
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  opacity: 0;
  transform: translateY(-16px);
  transition:
    opacity 0.25s ease,
    transform 0.3s ease,
    background-color 0.2s;
  /* Close in reverse order: the bottom link leaves first. */
  transition-delay: calc((2 - var(--i)) * 40ms);
}

/* Each link drops in a little after the one above it. */
.open .link {
  opacity: 1;
  transform: translateY(0);
  transition-delay: calc(var(--i) * 60ms);
}

.link:hover {
  background: var(--color-background-mute);
}

.link.router-link-exact-active {
  border-color: var(--color-heading);
  box-shadow: inset 3px 0 0 var(--color-heading);
}
</style>
