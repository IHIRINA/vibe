<template>
  <div class="magnet-page-container">
    <!-- 可伸缩侧边栏 -->
    <div class="sidebar" :class="{ 'sidebar-collapsed': isCollapsed }">
      <!-- 伸缩按钮 -->
      <div class="toggle-btn" @click="toggleSidebar">
        <el-icon :size="20">
          <ArrowRight v-if="isCollapsed" />
          <ArrowLeft v-else />
        </el-icon>
      </div>

      <div class="avatar-nickname" :class="{ 'hidden': isCollapsed }">
        <el-avatar 
          :size="60" 
          :src="userAvatar || tempAvatarUrl || 'https://cdn.jsdelivr.net/gh/qingruihappy/images@main/avatar.jpg'"
        ></el-avatar>
        <span class="nickname">{{ nickname }}</span>
      </div>

      <el-menu
        default-active="profile"
        class="el-menu-vertical-demo"
        @select="handleMenuSelect"
        :collapse="isCollapsed"
        background-color="rgba(240, 249, 240, 0.9)"
        text-color="#2e7d32"
        active-text-color="#1b5e20"
      >
        <el-menu-item index="home">
          <el-icon><Magnet /></el-icon>
          <template #title>磁贴</template>
        </el-menu-item>
        <el-menu-item index="people">
          <el-icon><User /></el-icon>
          <template #title>人们</template>
        </el-menu-item>
        <el-menu-item index="profile">
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

      <!-- 核心内容区（头像/昵称 + AI对话 + 输入栏） -->
      <div class="core-section">
        <!-- 头像&昵称设置区 -->
        <div class="profile-box">
          <div class="avatar-wrap">
            <el-avatar 
              :size="80" 
              :src="userAvatar || tempAvatarUrl || 'https://cdn.jsdelivr.net/gh/qingruihappy/images@main/avatar.jpg'"
            ></el-avatar>
            <el-button 
              type="primary" 
              class="avatar-set-btn"
              @click="avatarDialogVisible = true"
            >点击设置头像</el-button>
          </div>
          <div class="nickname-wrap">
            <el-input 
              v-model="nickname" 
              placeholder="点击设置昵称"
              class="nickname-input"
              @change="updateNickname"
            />
          </div>
        </div>

        <!-- AI对话区 -->
        <div class="ai-chat-box">
          <div class="ai-prompt">
            <p class="prompt-text">AI对话（提示：可以对磁贴中的内容提问）</p>
            <div class="suggest-questions">
              <el-tag 
                v-for="(q, idx) in suggestedQuestions" 
                :key="idx"
                @click="askAIQuestion(q)"
              >{{ q }}</el-tag>
            </div>
          </div>
          <div class="chat-display">
            <div 
              v-for="(msg, msgIdx) in aiConversation" 
              :key="msgIdx"
              class="chat-msg"
              :class="{ 'ai-msg': msg.isAI, 'user-msg': !msg.isAI }"
            >
              {{ msg.text }}
            </div>
          </div>
          <div class="chat-input">
            <el-input 
              v-model="aiInput" 
              placeholder="输入你的问题..."
              @keyup.enter="sendAIQuestion"
            >
              <template #append>
                <el-button 
                  icon="el-icon-send"
                  @click="sendAIQuestion"
                />
              </template>
            </el-input>
          </div>
        </div>

        <!-- 底部操作区（修改密码 + Stick On） -->
        <div class="bottom-actions">
          <el-button 
            type="primary" 
            class="password-btn"
            @click="passwordDialogVisible = true"
          >修改密码</el-button>
          <div class="stick-on-text">Stick On</div>
        </div>
      </div>

<el-dialog 
  v-model="avatarDialogVisible" 
  title="上传头像" 
  width="320px"
>
  <!-- 关键修改：添加触发按钮，确保点击能唤起文件选择框 -->
  <el-upload
    class="avatar-uploader"
    :show-file-list="false"
    :auto-upload="false"
    :on-change="handleAvatarChange"
  >
    <!-- 预览图区域 -->
    <div v-if="tempAvatarUrl" class="avatar-preview-container">
      <img 
        :src="tempAvatarUrl" 
        class="preview-avatar"
        alt="预览图"
      >
      <el-button 
        type="text" 
        class="reselect-btn"
        @click.stop="triggerFileSelect"
      >
        重新选择
      </el-button>
    </div>
    
    <!-- 未选择图片时的上传区域 -->
    <div v-else class="avatar-upload-trigger" @click.stop="triggerFileSelect">
      <i class="el-icon-plus avatar-upload-icon" />
      <span class="upload-text">点击选择图片</span>
    </div>
    
    <!-- 隐藏的文件输入框（关键） -->
    <input 
      type="file" 
      class="file-input" 
      accept="image/*" 
      @change="handleFileInputChange"
    >
  </el-upload>
  <template #footer>
    <span class="dialog-footer">
      <el-button @click="avatarDialogVisible = false">取消</el-button>
      <el-button 
        type="primary"
        @click="confirmAvatarUpdate"
        :loading="isUploading"
        style="background-color: #4caf50; border-color: #4caf50;"
      >确认上传</el-button>
    </span>
  </template>
</el-dialog>
      <el-dialog 
        v-model="passwordDialogVisible" 
        title="修改密码" 
        width="320px"
      >
        <el-form :model="passwordForm" label-width="80px">
          <el-form-item label="原密码">
            <el-input 
              v-model="passwordForm.oldPwd" 
              type="password"
            />
          </el-form-item>
          <el-form-item label="新密码">
            <el-input 
              v-model="passwordForm.newPwd" 
              type="password"
            />
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input 
              v-model="passwordForm.confirmPwd" 
              type="password"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="passwordDialogVisible = false">取消</el-button>
            <el-button 
              type="primary"
              @click="savePasswordChange"
              style="background-color: #4caf50; border-color: #4caf50;"
            >确认修改</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  ArrowLeft, ArrowRight, Magnet, User, UserFilled, 
} from '@element-plus/icons-vue'
import { useMainStore } from '../store'
import { getSayingAPI,  updatePasswordAPI, aiChatAPI, getUserInfoAPI } from '../api'
import type { SayingResponse,  UpdatePasswordForm, AIChatForm, UserInfoResponse } from '../api/type'

// 使用Pinia store
const mainStore = useMainStore()
const router = useRouter()

// 计算属性，从store获取用户信息
const nickname = computed(() => mainStore.nickname)
const userAvatar = computed(() => mainStore.userAvatar)

// 页面加载时初始化
onMounted(async() => {
  fetchSaying()
  await fetchUserInfo()
})

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const response = await getUserInfoAPI()
    const res: UserInfoResponse = response.data
    if (res.code === 0) {
      // 更新store中的用户信息
      mainStore.updateNickname(res.data.nickname)
      mainStore.updateAvatar(res.data.avatar)
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 响应式数据
const isCollapsed = ref(false)
const slogan = ref('今日事，今日毕')

// 对话框控制
const avatarDialogVisible = ref(false)
const passwordDialogVisible = ref(false)

// AI对话相关
const suggestedQuestions = ref([
  '评价一下过去的一个月我过得如何',
  '给我一些生活小建议',
  '分析下我最近的磁贴内容'
])
const aiConversation = ref<{ isAI: boolean; text: string }[]>([])
const aiInput = ref('')

// 头像上传相关（完善部分）
const tempAvatarUrl = ref('')
const isUploading = ref(false)  // 上传状态标识
const selectedAvatarFile = ref<File | null>(null)  // 保存选中的文件对象

// 密码修改
const passwordForm = ref({
  oldPwd: '',
  newPwd: '',
  confirmPwd: ''
})

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


// AI对话交互
const askAIQuestion = (question: string) => {
  aiInput.value = question
  sendAIQuestion()
}
const sendAIQuestion = async () => {
  if (!aiInput.value.trim()) return
  
  aiConversation.value.push({ isAI: false, text: aiInput.value })
  
  try {
    const data: AIChatForm = {
      content: aiInput.value
    }
    const response = await aiChatAPI(data)
    
    if (response.data.code === 200) {
      aiConversation.value.push({ 
        isAI: true, 
        text: response.data.data.reply
      })
    } else {
      ElMessage.error(response.data.msg || 'AI回复失败')
    }
  } catch (error) {
    console.error('AI聊天失败:', error)
    ElMessage.error('AI回复失败，请稍后重试')
  }
  
  aiInput.value = ''
}

// 新增：获取文件输入框并触发点击
const triggerFileSelect = () => {
  const fileInput = document.querySelector('.file-input') as HTMLInputElement;
  if (fileInput) {
    // 重置输入框（解决重复选择同一文件不触发change事件的问题）
    fileInput.value = '';
    // 触发点击
    fileInput.click();
  }
}

// 新增：处理文件输入框的选择事件
const handleFileInputChange = (e: Event) => {
  const target = e.target as HTMLInputElement;
  if (target.files && target.files[0]) {
    // 将选择的文件传递给原处理方法
    handleAvatarChange({ raw: target.files[0] });
  }
}

// 保持原有的handleAvatarChange方法不变，补充类型定义
const handleAvatarChange = (file: { raw: File }) => {
  // 保存选中的文件
  selectedAvatarFile.value = file.raw;
  
  // 读取文件并显示预览
  const reader = new FileReader();
  reader.onload = (e) => {
    tempAvatarUrl.value = e.target?.result as string;
  };
  reader.readAsDataURL(file.raw);
}

// 确认头像更新（纯前端实现）
const confirmAvatarUpdate = () => {
  // 验证是否有选中的图片
  if (!tempAvatarUrl.value || !selectedAvatarFile.value) {
    ElMessage.warning('请先选择并预览图片')
    return
  }
  
  // 显示加载状态
  isUploading.value = true
  
  // 模拟处理过程（实际项目中可根据需要调整延迟时间）
  setTimeout(() => {
    try {
      // 1. 更新store中的头像
      mainStore.updateAvatar(tempAvatarUrl.value)
      
      // 2. 重置状态并关闭对话框
      tempAvatarUrl.value = mainStore.userAvatar
      selectedAvatarFile.value = null
      avatarDialogVisible.value = false
      ElMessage.success('头像更新成功')
    } catch (error) {
      console.error('头像更新失败:', error)
      ElMessage.error('头像更新失败，请重试')
    } finally {
      // 隐藏加载状态
      isUploading.value = false
    }
  }, 800)  // 800ms延迟，提升用户体验
}

// 密码修改逻辑
const savePasswordChange = async () => {
  if (passwordForm.value.newPwd !== passwordForm.value.confirmPwd) {
    ElMessage.error('两次输入的密码不一致！')
    return
  }
  
  if (!passwordForm.value.oldPwd || !passwordForm.value.newPwd) {
    ElMessage.error('请填写完整信息！')
    return
  }
  
  try {
    const data: UpdatePasswordForm = {
      oldPassword: passwordForm.value.oldPwd,
      newPassword: passwordForm.value.newPwd
    }
    
    const response = await updatePasswordAPI(data)
    
    if (response.data.code === 200) {
      ElMessage.success('密码修改成功')
      passwordDialogVisible.value = false
      passwordForm.value.oldPwd = ''
      passwordForm.value.newPwd = ''
      passwordForm.value.confirmPwd = ''
    } else {
      ElMessage.error(response.data.msg || '密码修改失败')
    }
  } catch (error) {
    console.error('密码修改失败:', error)
    ElMessage.error('密码修改失败，请稍后重试')
  }
}

// 更新昵称
const updateNickname = () => {
  mainStore.updateNickname(nickname.value)
}

</script>

<style scoped lang="scss">
// 整体布局
.magnet-page-container {
  display: flex;
  height: 100vh;
  background: linear-gradient(135deg, #f0f9f0 0%, #e8f5e9 100%);
}

// 侧边栏样式（保持原样）
.sidebar {
  width: 220px;
  background: rgba(240, 249, 240, 0.9);
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #c8e6c9;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  position: relative;
  padding-top: 20px;
  align-items: center;

  &-collapsed {
    width: 80px;
  }
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
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;

  &.hidden {
    padding: 20px 0;
    opacity: 0;
    pointer-events: none;
    transform: scale(0.8);
  }
}

.nickname {
  display: block;
  margin-top: 15px;
  font-size: 18px;
  font-weight: 500;
  color: #1b5e20;
}

.el-menu-vertical-demo {
  width: 100%;
}

// 主内容区样式
.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.top-bar {
  margin-bottom: 30px;

  .slogan {
    background: linear-gradient(135deg, #a5d6a7 0%, #81c784 100%);
    color: #1b5e20;
    padding: 12px 20px;
    border-radius: 30px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    font-weight: 500;
    max-width: 400px;
  }
}

/* 核心内容区整体容器 */
.core-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
  background: rgba(240, 255, 242, 0.8); /* 浅绿透明背景，和侧边栏呼应 */
  border-radius: 12px;
  border: 1px solid #c8e6c9;
  box-shadow: 0 2px 10px rgba(129, 199, 132, 0.1);
}

/* 头像&昵称区 */
/* 头像上传区域样式补充 */
.avatar-upload-trigger {
  width: 100%;
  height: 180px;
  border: 2px dashed #b3d9b5;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    border-color: #4caf50;
    background-color: rgba(76, 175, 80, 0.05);
  }
}

.avatar-upload-icon {
  font-size: 36px;
  color: #81c784;
  margin-bottom: 10px;
}

.upload-text {
  color: #558b2f;
  font-size: 14px;
}

.avatar-preview-container {
  width: 100%;
  height: 180px;
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e8f5e9;
}

.preview-avatar {
  width: 100%;
  height: 100%;
  object-fit: contain;
  background-color: #f9fbe7;
}

.reselect-btn {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 5px 0;
  margin: 0;
  
  &:hover {
    background-color: rgba(0, 0, 0, 0.7);
    color: white;
  }
}

/* 隐藏原生文件输入框，但保持功能可用 */
.file-input {
  display: none;
}
.profile-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: rgba(255, 255, 255, 0.9); /* 白色微透，突出内容 */
  border: 1px solid #b3d9b5; /* 淡绿色边框 */
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(165, 214, 167, 0.2);
  transition: all 0.3s ease;
}
.profile-box:hover {
  box-shadow: 0 4px 12px rgba(129, 199, 132, 0.3);
  transform: translateY(-2px);
}

.avatar-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 15px;
}
.avatar-set-btn {
  background: #4caf50; /* 绿色按钮 */
  border: 1px solid #4caf50;
  color: #fff;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s ease;
}
.avatar-set-btn:hover {
  background: #388e3c; /* 深一点的绿色 */
  border-color: #388e3c;
}

.nickname-wrap {
  width: 100%;
}
.nickname-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #b3d9b5;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.8);
  transition: all 0.3s ease;
}
.nickname-input:focus {
  border-color: #4caf50;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
  outline: none;
}

/* AI对话区 */
.ai-chat-box {
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid #b3d9b5;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(165, 214, 167, 0.2);
  transition: all 0.3s ease;
}
.ai-chat-box:hover {
  box-shadow: 0 4px 12px rgba(129, 199, 132, 0.3);
  transform: translateY(-2px);
}

.ai-prompt {
  font-size: 14px;
  color: #555;
  margin-bottom: 10px;
}
.suggest-questions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 15px;
}
.suggest-questions .el-tag {
  background: #e8f5e9; /* 浅绿标签背景 */
  color: #4caf50; /* 绿色文字 */
  border: 1px solid #c8e6c9;
  cursor: pointer;
  transition: all 0.3s ease;
}
.suggest-questions .el-tag:hover {
  background: #4caf50;
  color: #fff;
  border-color: #4caf50;
}

.chat-display {
  height: 200px;
  overflow-y: auto;
  border: 1px solid #b3d9b5;
  border-radius: 8px;
  padding: 10px;
  margin-bottom: 10px;
  background: rgba(255, 255, 255, 0.8);
  transition: all 0.3s ease;
}
.chat-display::-webkit-scrollbar {
  width: 6px;
}
.chat-display::-webkit-scrollbar-thumb {
  background: #b3d9b5;
  border-radius: 3px;
}
.chat-display::-webkit-scrollbar-track {
  background: #f9f9f9;
}

.chat-msg {
  max-width: 70%;
  padding: 8px 12px;
  border-radius: 8px;
  margin-bottom: 8px;
  line-height: 1.4;
}
.ai-msg {
  background: #f5f5f5;
  color: #333;
  align-self: flex-start;
  border: 1px solid #eee;
}
.user-msg {
  background: #e8f5e9;
  color: #2e7d32;
  align-self: flex-end;
  border: 1px solid #c8e6c9;
  margin-left: auto;
}

.chat-input {
  display: flex;
  gap: 10px;
}
.chat-input .el-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #b3d9b5;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.8);
  transition: all 0.3s ease;
}
.chat-input .el-input:focus {
  border-color: #4caf50;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
  outline: none;
}
.chat-input .el-button {
  background: #4caf50;
  border: 1px solid #4caf50;
  color: #fff;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s ease;
}
.chat-input .el-button:hover {
  background: #388e3c;
  border-color: #388e3c;
}

/* 底部操作区 */
.bottom-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}
.password-btn {
  background: #4caf50;
  border: 1px solid #4caf50;
  color: #fff;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s ease;
}
.password-btn:hover {
  background: #388e3c;
  border-color: #388e3c;
}
.stick-on-text {
  font-style: italic;
  color: #1b5e20;
  opacity: 0.9;
}

// 响应式设计
@media (max-width: 768px) {
  .magnet-page-container {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
    height: auto;
    border-right: none;
    border-bottom: 1px solid #c8e6c9;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    padding: 10px 20px;

    .el-menu-vertical-demo {
      display: flex;
      flex-direction: row;
    }

    .avatar-nickname {
      flex-direction: row;
      gap: 10px;
      margin-bottom: 0;
      padding: 15px;
    }
  }
  
  .sidebar-collapsed {
    width: 100%;
  }
  
  .main-content {
    padding: 20px;
  }

  .core-section {
    gap: 15px;
  }

  .chat-display {
    height: 150px;
  }

  .bottom-actions {
    flex-direction: column;
    gap: 10px;
    align-items: flex-start;
  }
}

@media (max-width: 576px) {
  .profile-box,
  .ai-chat-box {
    padding: 15px;
  }

  .chat-msg {
    max-width: 80%;
  }
}
</style>