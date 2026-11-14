import { createRouter, createWebHistory } from 'vue-router'
import PhotoGallery from '../components/PhotoGallery.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: PhotoGallery,
    },
    {
      // The gallery is the homepage now; keep old /gallery links working.
      path: '/gallery',
      redirect: '/',
    },
    {
      path: '/userpage',
      name: 'userpage',
      component: () => import('../views/UserPageView.vue'),
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    },
  ],
})

export default router
