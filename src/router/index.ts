import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/MagnetPage.vue'
import Login from '../views/Login.vue'
import My from '../views/profile.vue'
import Person from '../views/Person.vue'

// 定义路由
const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/home',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/profile',
    name: 'My',
    component: My
  },
  {
    path: '/people',
    name: 'Person',
    component: Person

  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router