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

      <!-- 磁贴列表（核心内容区） -->
      <div class="magnet-list">
        <!-- 人物分组循环 -->
        <div 
          v-for="(personItem, personIndex) in magnetList" 
          :key="personItem.id" 
          class="person-group"
        >
          <!-- 人物标题 -->
          <div class="person-title" @click="openPersonDialog(personIndex)">
            {{ personItem.name }}
          </div>
          
          <!-- 事件项循环 -->
          <div class="events-container">
            <div 
              class="event-item" 
              v-for="(eventItem, eventIndex) in personItem.tiles" 
              :key="eventItem.id"
              :style="{ background: getGreenGradient(personIndex * magnetList.length + eventIndex) }"
            >
              <div class="event-content">
                <div class="event-name">{{ eventItem.content }}</div>
                <div class="event-desc">{{ eventItem.content }}</div>
                <div class="event-date">{{ formatDate(eventItem.created_at) }}</div>
              </div>
              
              <!-- 修改按钮 -->
              <button 
                class="edit-btn" 
                @click.stop="handleEditMagnet(personIndex, eventIndex)"
                aria-label="修改事件"
              >
                <el-icon><Edit /></el-icon>
                <span class="edit-text">查看与修改</span>
              </button>
              
              <!-- 删除按钮 -->
              <button 
                class="delete-btn" 
                @click.stop="handleDeleteMagnet(personIndex, eventIndex)"
                aria-label="删除事件"
              >
                <el-icon><Close /></el-icon>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="footer">Stick On</div>
    </div>

    <!-- 人物详情弹窗 -->
    <div v-if="dialogVisible" class="person-dialog">
      <div class="dialog-content">
        <div class="dialog-header">
          <button 
            class="edit-name-btn" 
            @click="toggleNameEdit"
          >
            {{ isEditingName ? '确定' : '修改人物名称' }}
          </button>
          <button class="close-btn" @click="closePersonDialog">
            <el-icon><Close /></el-icon>
          </button>
        </div>
        
        <div class="dialog-body">
          <div v-if="selectedPersonIndex >= 0" class="person-name">
            <input 
              v-if="isEditingName" 
              v-model="newName" 
              class="name-input"
            />
            <span v-else class="name-text">
              {{ magnetList[selectedPersonIndex].name }}
            </span>
          </div>
        </div>
        
        <div class="dialog-footer">
          <button class="ai-btn" @click="generateStory">
            AI生成ta和我的故事
          </button>
        </div>
      </div>
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
      :initial-data="getCurrentEventData()"
      :current-person-index="currentEditPersonIndex"
      :current-event-index="currentEditEventIndex"
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
import { getSayingAPI, createTileAPI, updateTileAPI, deleteTileAPI, getPersonsAPI, analyzePersonAPI, updatePersonAPI } from '../api'
import type { SayingResponse, CreateTileResponse, UpdateTileResponse, GetPersonsResponse, PersonTile, Person as APIPerson, AnalyzePersonResponse, UpdatePersonResponse } from '../api/type'
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
const magnetList = ref<APIPerson[]>([
  {
    id: 0,
    name: '示例人物',
    tiles: [
      { id: 1, content: '这是第一个示例磁贴的内容', created_at: '' },
      { id: 2, content: '这是第二个示例磁贴的内容', created_at: '' },
      { id: 3, content: '这是第三个示例磁贴的内容', created_at: '' }
    ]
  }
]) // 人物列表数据

// 弹窗相关数据
const dialogVisible = ref(false)
const selectedPersonIndex = ref(-1)
const isEditingName = ref(false)
const newName = ref('')

// 昵称和头像从 Pinia 中获取
const nickname = computed(() => mainStore.userInfo?.nickname || '昵称')
const userAvatar = computed(() => mainStore.userInfo?.avatar)

// 控制对话框显示
const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const currentEditPersonIndex = ref(-1)
const currentEditEventIndex = ref(-1)

// 获取当前编辑的事件数据
const getCurrentEventData = () => {
  if (currentEditPersonIndex.value < 0 || currentEditEventIndex.value < 0) {
    return { name: '', content: '', color: '#4caf50' }
  }
  const person = magnetList.value[currentEditPersonIndex.value]
  const tile = person.tiles[currentEditEventIndex.value]
  return {
    name: tile.content,
    content: tile.content,
    color: '#4caf50'
  }
}

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
    console.log(response)
    if (response.data.code === 200) {
      slogan.value = response.data.data
    }
  } catch (error) {
    console.error('获取每日一句失败:', error)
  }
}

// 获取人物列表数据
const fetchPersons = async () => {
  try {
    const response = await getPersonsAPI()
    const res: GetPersonsResponse = response.data
    if (res.code === 200) {
      magnetList.value = res.data
    } else {
      ElMessage.error(res.msg || '获取人物列表失败')
    }
  } catch (error) {
    console.error('获取人物列表失败:', error)
    ElMessage.error('获取人物列表失败')
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
      // 假设添加到第一个人物的事件列表中，实际应根据业务逻辑调整
      if (magnetList.value.length > 0) {
        magnetList.value[0].tiles.push({
          id: res.data.id,
          content: res.data.content,
          created_at: res.data.created_at
        })
      }
      ElMessage.success('事件添加成功')
    } else {
      ElMessage.error(res.msg || '添加失败')
    }
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

// 打开人物弹窗
const openPersonDialog = (personIndex: number) => {
  selectedPersonIndex.value = personIndex
  newName.value = magnetList.value[personIndex].name
  dialogVisible.value = true
}

// 关闭人物弹窗
const closePersonDialog = () => {
  dialogVisible.value = false
  isEditingName.value = false
}

// 切换名称编辑状态
const toggleNameEdit = () => {
  if (isEditingName.value) {
    // 确认修改名称
    updatePersonName()
  } else {
    // 开始编辑名称
    isEditingName.value = true
  }
}

// 更新人物名称
const updatePersonName = async () => {
  if (selectedPersonIndex.value < 0) return
  
  const person = magnetList.value[selectedPersonIndex.value]
  try {
    const response = await updatePersonAPI(person.id, { name: newName.value })
    const res: UpdatePersonResponse = response.data
    
    if (res.code === 200) {
      magnetList.value[selectedPersonIndex.value].name = res.data.name
      isEditingName.value = false
      ElMessage.success('人物名称更新成功')
    } else {
      ElMessage.error(res.msg || '更新失败')
    }
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

// AI生成故事
const generateStory = async () => {
  if (selectedPersonIndex.value < 0) return
  
  const person = magnetList.value[selectedPersonIndex.value]
  try {
    // 调用AI分析接口
    const response = await analyzePersonAPI(person.id)
    const res: AnalyzePersonResponse = response.data
    
    if (res.code === 200) {
      // 使用分析结果创建新的磁贴
      const tileResponse = await createTileAPI({ content: res.data.summary })
      const tileRes: CreateTileResponse = tileResponse.data
      
      if (tileRes.code === 200) {
        // 添加新磁贴到当前人物
        magnetList.value[selectedPersonIndex.value].tiles.push({
          id: tileRes.data.id,
          content: tileRes.data.content,
          created_at: tileRes.data.created_at
        })
        ElMessage.success('故事已生成并添加到磁贴')
      } else {
        ElMessage.error(tileRes.msg || '创建磁贴失败')
      }
    } else {
      ElMessage.error(res.msg || 'AI分析失败')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 处理编辑磁贴
const handleEditMagnet = (personIndex: number, eventIndex: number) => {
  currentEditPersonIndex.value = personIndex
  currentEditEventIndex.value = eventIndex
  editDialogVisible.value = true
}

// 处理保存编辑
const handleSaveEdit = async (data: any) => {
  const pIndex = currentEditPersonIndex.value
  const eIndex = currentEditEventIndex.value
  if (pIndex < 0 || eIndex < 0) return
  
  try {
    const currentPerson = magnetList.value[pIndex]
    const currentEvent = currentPerson.tiles[eIndex]
    const response = await updateTileAPI(currentEvent.id, {
      content: data.content
    })
    
    const res: UpdateTileResponse = response.data
    if (res.code === 200) {
      magnetList.value[pIndex].tiles[eIndex] = {
        ...currentEvent,
        content: res.data.content
      } as PersonTile
      ElMessage.success('事件更新成功')
    } else {
      ElMessage.error(res.msg || '更新失败')
    }
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

// 删除磁贴
const handleDeleteMagnet = (personIndex: number, eventIndex: number) => {
  ElMessageBox.confirm('确认删除该事件吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const currentPerson = magnetList.value[personIndex]
      const currentEvent = currentPerson.tiles[eventIndex]
      const response = await deleteTileAPI(currentEvent.id)
      const res = response.data
      
      if (res.code === 200) {
        currentPerson.tiles.splice(eventIndex, 1)
        ElMessage.success('事件删除成功')
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

// 页面加载时获取数据
onMounted(() => {
  fetchSaying()
  fetchPersons() // 加载人物列表数据
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

/* 人物分组样式 */
.person-group {
  margin-bottom: 30px;
  padding: 15px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.person-title {
  font-size: 20px;
  font-weight: bold;
  color: #2e7d32;
  margin-bottom: 15px;
  padding-bottom: 8px;
  border-bottom: 2px solid #a5d6a7;
  cursor: pointer;
}

/* 事件容器样式 */
.events-container {
  display: flex;
  overflow-x: auto;
  padding: 10px 0;
  gap: 15px;
}

/* 事件项样式 */
.event-item {
  min-width: 200px;
  height: 200px;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  padding: 20px;
  position: relative;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
  flex-shrink: 0;
}

.event-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
}

.event-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: white;
  text-align: center;
}

.event-name {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 10px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.event-desc {
  font-size: 14px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.event-date {
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

/* 人物弹窗样式 */
.person-dialog {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.dialog-content {
  width: 500px;
  background: linear-gradient(135deg, #4caf50 0%, #81c784 100%);
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  padding: 20px;
  position: relative;
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 30px;
}

.edit-name-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  color: white;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.3s;
}

.edit-name-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.close-btn {
  width: 32px;
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.dialog-body {
  text-align: center;
  margin-bottom: 30px;
}

.person-name {
  font-size: 28px;
  font-weight: bold;
  color: white;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.name-input {
  background: rgba(255, 255, 255, 0.9);
  border: none;
  border-radius: 4px;
  padding: 10px 15px;
  font-size: 24px;
  font-weight: bold;
  width: 80%;
  text-align: center;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
}

.ai-btn, .cancel-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 4px;
  padding: 10px 20px;
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s;
}

.ai-btn:hover, .cancel-btn:hover {
  background: rgba(255, 255, 255, 0.3);
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
  
  .event-item {
    min-width: 150px;
    height: 150px;
  }
  
  .dialog-content {
    width: 90%;
    margin: 0 5%;
  }
  
  .person-name {
    font-size: 24px;
  }
  
  .name-input {
    font-size: 20px;
  }
}
</style>