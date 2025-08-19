<template>
  <div class="magnet-page-container">
    <!-- 可伸缩侧边栏 -->
    <div class="sidebar" :class="{ 'sidebar-collapsed': isCollapsed }">
      <!-- 伸缩按钮 -->
      <div class="toggle-btn" @click="toggleSidebar">
        <el-icon>
          <ArrowRight v-if="isCollapsed" />
          <ArrowLeft v-else />
        </el-icon>
      </div>

      <div class="avatar-nickname" :class="{ 'hidden': isCollapsed }">
        <el-avatar 
          :size="60" 
          :src="userAvatar || 'https://cdn.jsdelivr.net/gh/qingruihappy/images@main/avatar.jpg'"
        ></el-avatar>
        <span class="nickname">{{ nickname }}</span>
      </div>

      <el-menu
        default-active="/home"
        class="el-menu-vertical-demo"
        @select="handleMenuSelect"
        :collapse="isCollapsed"
        background-color="rgba(240, 249, 240, 0.9)"
        text-color="#2e7d32"
        active-text-color="#1b5e20"
      >
        <el-menu-item index="/home">
          <el-icon><Magnet /></el-icon>
          <template #title>磁贴</template>
        </el-menu-item>
        <el-menu-item index="/people">
          <el-icon><User /></el-icon>
          <template #title>人们</template>
        </el-menu-item>
        <el-menu-item index="/profile">
          <el-icon><UserFilled /></el-icon>
          <template #title>我</template>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 右侧主要内容区域 -->
    <div class="main-content">
      <!-- 顶部区域 -->
      <div class="top-bar">
        <div class="slogan">
          {{ slogan }}
        </div>

      </div>

      <div class="footer">Stick On</div>
    </div>


  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft, ArrowRight, Magnet, User, UserFilled } from '@element-plus/icons-vue'
import { useMainStore } from '../store'
import { getSayingAPI } from '../api'
import type { SayingResponse } from '../api/type'

// 使用 Pinia store
const mainStore = useMainStore()
const router = useRouter()

// 响应式数据
const isCollapsed = ref(false)
const slogan = ref('今日事，今日毕')

// 昵称和头像从 Pinia 中获取
const nickname = computed(() => mainStore.userInfo?.nickname || '昵称')
const userAvatar = computed(() => mainStore.userInfo?.avatar)

// 切换侧边栏
const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}

// 菜单选择
const handleMenuSelect = (index: string) => {
  router.push(index)
}

// 获取每日一句
const fetchSaying = async () => {
  try {
    const response = await getSayingAPI()
    const res: SayingResponse = response.data
    if (res.code === 200) {
      slogan.value = res.data.text
    }
  } catch (error) {
    console.error('获取每日一句失败:', error)
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchSaying()
})
</script>

<style scoped>
.magnet-page-container {
  display: flex;
  height: 100vh;
  background: linear-gradient(135deg, #f0f9f0 0%, #e8f5e9 100%);
}

.sidebar {
  width: 220px;
  background: rgba(240, 249, 240, 0.9);
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #c8e6c9;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  position: relative;
}

.sidebar-collapsed {
  width: 80px;
}

.toggle-btn {
  position: absolute;
  top: 15px;
  right: -12px;
  width: 24px;
  height: 24px;
  background: #4caf50;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: white;
  z-index: 10;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.avatar-nickname {
  padding: 30px 20px 20px;
  text-align: center;
  transition: all 0.3s ease;
}

.avatar-nickname.hidden {
  padding: 20px 0;
}

.nickname {
  display: block;
  margin-top: 15px;
  font-size: 18px;
  font-weight: 500;
  color: #1b5e20;
}

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.top-bar {
  margin-bottom: 30px;
}

.slogan {
  background: linear-gradient(135deg, #a5d6a7 0%, #81c784 100%);
  color: #1b5e20;
  padding: 12px 20px;
  border-radius: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  font-weight: 500;
  max-width: 400px;
}

.footer {
  position: fixed;
  bottom: 20px;
  right: 20px;
  font-style: italic;
  color: #1b5e20;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .magnet-page-container {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
    height: auto;
    border-right: none;
    border-bottom: 1px solid #c8e6c9;
  }
  
  .sidebar-collapsed {
    width: 100%;
  }
  
  .avatar-nickname {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 15px;
    padding: 15px;
  }
  
  .avatar-nickname.hidden .nickname {
    display: none;
  }
  
  .el-menu {
    display: flex;
  }
  
  .el-menu-item {
    flex: 1;
    text-align: center;
  }
}
</style>