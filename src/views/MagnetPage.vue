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
        <div class="filter-bar">
          <el-button type="primary" @click="handleFilter" class="green-btn">筛选</el-button>
          <el-date-picker
            v-model="startDate"
            type="date"
            placeholder="开始日期"
            style="margin: 0 10px;"
          ></el-date-picker>
          <span>到</span>
          <el-date-picker
            v-model="endDate"
            type="date"
            placeholder="结束日期"
            style="margin: 0 10px;"
          ></el-date-picker>
          <el-button
            type="primary"
            :icon="Plus"
            @click="openAddDialog"  
            circle
            class="green-btn"
          ></el-button>
        </div>
      </div>

      <!-- 磁贴列表 -->
      <div class="magnet-list">
        <div
          class="magnet-item"
          v-for="(item, index) in magnetList"
          :key="item.id"
          :style="{ background: getGreenGradient(index) }"
        >
          <div class="magnet-content">
            <div class="magnet-name">{{ item.name }}</div>
            <div class="magnet-desc">{{ item.content }}</div>
            <div class="magnet-date">{{ formatDate(item.created_at) }}</div>
          </div>
          
          <!-- 修改按钮 -->
          <button 
            class="edit-btn" 
            @click.stop="handleEditMagnet(index)"
            aria-label="修改磁贴"
          >
            <el-icon><Edit /></el-icon>
            <span class="edit-text">查看与修改</span>
          </button>
          
          <!-- 删除按钮 -->
          <button 
            class="delete-btn" 
            @click.stop="handleDeleteMagnet(index)"
            aria-label="删除磁贴"
          >
            <el-icon><Close /></el-icon>
          </button>
        </div>
      </div>

      <div class="footer">Stick On</div>
    </div>

    <!-- 新增磁贴对话框 -->
    <MagnetAddDialog
      v-model:visible="addDialogVisible"
      :initial-data="{
        name: '',
        content: '',
        color: '#4caf50'
      }"
      @save="handleAddMagnet"
    />

    <!-- 编辑磁贴对话框 -->
    <MagnetEditDialog
      v-model:visible="editDialogVisible"
      :initial-data="magnetList[currentEditIndex] || {}"
      :current-index="currentEditIndex"
      @save="handleSaveEdit"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowLeft,
  ArrowRight,
  Magnet,
  User,
  UserFilled,
  Plus,
  Edit,
  Close
} from '@element-plus/icons-vue'
import { useMainStore } from '../store'
import { getSayingAPI, createTileAPI, updateTileAPI, deleteTileAPI, getTilesAPI } from '../api'
import type { SayingResponse, CreateTileResponse, UpdateTileResponse, GetTilesResponse } from '../api/type'
import MagnetAddDialog from '../components/MagnetAddDialog.vue'
import MagnetEditDialog from '../components/MagnetEditDialog.vue'

// 使用 Pinia store
const mainStore = useMainStore()
const router = useRouter()

// 响应式数据
const isCollapsed = ref(false)
const slogan = ref('今日事，今日毕')
const startDate = ref('')
const endDate = ref('')
const magnetList = ref([
  { id: 1, name: 'stick 1', content: '这是第一个磁贴的内容', created_at: '2023-01-01T00:00:00Z' },
  { id: 2, name: 'stick 2', content: '这是第二个磁贴的内容', created_at: '2023-01-02T00:00:00Z' },
  { id: 3, name: 'stick 3', content: '这是第三个磁贴的内容', created_at: '2023-01-03T00:00:00Z' }
])

// 昵称和头像从 Pinia 中获取
const nickname = mainStore.userInfo?.nickname || '未设置昵称'
const userAvatar = computed(() => mainStore.userInfo?.avatar)

// 控制对话框显示
const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const currentEditIndex = ref(-1)

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
    if (response.data.code === 200) {
      slogan.value = response.data.data
    }
  } catch (error) {
    console.error('获取每日一句失败:', error)
  }
}

// 筛选处理
const handleFilter = () => {
  console.log('筛选日期范围:', {
    start: startDate.value,
    end: endDate.value
  })
}

// 打开新增对话框
const openAddDialog = () => {
  addDialogVisible.value = true
}

// 处理新增磁贴
const handleAddMagnet = async (data: any) => {
  try {
    const response = await createTileAPI({
      content: data.content
    })
    
    const res: CreateTileResponse = response.data
    if (res.code === 200) {
      magnetList.value.push({
        id: res.data.id,
        name: `磁贴 ${res.data.id}`,
        content: res.data.content,
        created_at: res.data.created_at
      })
      ElMessage.success('磁贴添加成功')
    } else {
      ElMessage.error(res.msg || '添加失败')
    }
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

// 处理编辑磁贴
const handleEditMagnet = (index: number) => {
  currentEditIndex.value = index
  editDialogVisible.value = true
}

// 处理保存编辑
const handleSaveEdit = async (data: any) => {
  const index = currentEditIndex.value
  if (index < 0) return
  
  try {
    const currentItem = magnetList.value[index]
    const response = await updateTileAPI(currentItem.id, {
      content: data.content
    })
    
    const res: UpdateTileResponse = response.data
    if (res.code === 200) {
      magnetList.value[index] = {
        ...magnetList.value[index],
        content: res.data.content
      }
      ElMessage.success('磁贴更新成功')
    } else {
      ElMessage.error(res.msg || '更新失败')
    }
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

// 删除磁贴
const handleDeleteMagnet = (index: number) => {
  ElMessageBox.confirm('确认删除该磁贴吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const currentItem = magnetList.value[index]
      const response = await deleteTileAPI(currentItem.id)
      const res = response.data
      
      if (res.code === 200) {
        magnetList.value.splice(index, 1)
        ElMessage.success('磁贴删除成功')
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 获取绿色渐变背景
const getGreenGradient = (index: number) => {
  const gradients = [
    'linear-gradient(135deg, #4caf50 0%, #81c784 100%)',
    'linear-gradient(135deg, #66bb6a 0%, #a5d6a7 100%)',
    'linear-gradient(135deg, #81c784 0%, #c8e6c9 100%)',
    'linear-gradient(135deg, #388e3c 0%, #66bb6a 100%)',
    'linear-gradient(135deg, #2e7d32 0%, #4caf50 100%)',
    'linear-gradient(135deg, #1b5e20 0%, #388e3c 100%)',
    'linear-gradient(135deg, #c8e6c9 0%, #e8f5e9 100%)',
    'linear-gradient(135deg, #a5d6a7 0%, #c8e6c9 100%)'
  ]
  return gradients[index % gradients.length]
}

// 格式化日期显示
const formatDate = (dateString: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

// 获取磁贴列表
const fetchTiles = async () => {
  try {
    const response = await getTilesAPI()
    if (response.data.code === 200) {
      // 保留前三个示例磁贴，然后添加从API获取的数据
      const exampleTiles = magnetList.value.slice(0, 3)
      const apiTiles = response.data.data.map(tile => ({
        id: tile.id,
        name: `磁贴 ${tile.id}`,
        content: tile.content,
        created_at: tile.created_at
      }))
      magnetList.value = [...exampleTiles, ...apiTiles]
    }
  } catch (error) {
    console.error('获取磁贴列表失败:', error)
    ElMessage.error('获取磁贴列表失败')
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchSaying()
  fetchTiles()
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  gap: 20px;
}

.slogan {
  background: linear-gradient(135deg, #a5d6a7 0%, #81c784 100%);
  color: #1b5e20;
  padding: 12px 20px;
  border-radius: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  font-weight: 500;
  flex: 1;
  max-width: 400px;
}

.filter-bar {
  display: flex;
  align-items: center;
  gap: 10px;
}

.green-btn {
  background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%);
  border: none;
  color: white;
}

.green-btn:hover {
  background: linear-gradient(135deg, #388e3c 0%, #2e7d32 100%);
}

.magnet-add {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.magnet-add .green-btn {
  width: 40px;
  height: 40px;
  min-width: 40px;
  min-height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.magnet-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.magnet-item {
  aspect-ratio: 1;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  padding: 20px;
  position: relative;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
}

.magnet-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
}

.magnet-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: white;
  text-align: center;
}

.magnet-name {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 10px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.magnet-desc {
  font-size: 14px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.magnet-date {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.9);
  margin-top: 10px;
  font-style: italic;
}

.edit-btn {
  position: absolute;
  bottom: 10px;
  left: 10px;
  background: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 4px;
  padding: 4px 8px;
  color: white;
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.edit-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.edit-text {
  font-size: 12px;
}

.delete-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 24px;
  height: 24px;
  background: rgba(255, 255, 255, 0.3);
  border: none;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.delete-btn:hover {
  background: rgba(255, 255, 255, 0.5);
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
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
@media (max-width: 1200px) {
  .magnet-list {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 992px) {
  .magnet-list {
    grid-template-columns: repeat(2, 1fr);
  }
}

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
  
  .top-bar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .slogan {
    max-width: 100%;
    margin-bottom: 15px;
  }
  
  .filter-bar {
    flex-wrap: wrap;
    justify-content: center;
  }
}

@media (max-width: 576px) {
  .magnet-list {
    grid-template-columns: 1fr;
  }
  
  .main-content {
    padding: 15px;
  }
}
</style>