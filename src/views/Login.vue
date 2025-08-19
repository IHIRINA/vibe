<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { registerAPI, loginAPI } from '../api'
import type { RegisterForm, LoginForm, RegisterResponse, LoginResponse } from '../api/type'

// 表单数据
const form = reactive({
  username: '',
  password: '',
  nickname: ''
})

// 错误信息
const errors = reactive({
  username: '',
  password: '',
  nickname: ''
})

// 加载状态
const loading = ref(false)

// 路由
const router = useRouter()

// 验证用户名
const validateUsername = () => {
  if (!form.username.trim()) {
    errors.username = '用户名不能为空'
  } else if (form.username.trim().length < 3) {
    errors.username = '用户名至少3个字符'
  } else {
    errors.username = ''
  }
}

// 验证密码
const validatePassword = () => {
  if (!form.password) {
    errors.password = '密码不能为空'
  } else if (form.password.length < 6) {
    errors.password = '密码至少6个字符'
  } else {
    errors.password = ''
  }
}

// 验证昵称
const validateNickname = () => {
  if (form.nickname && form.nickname.length > 6) {
    errors.nickname = '昵称不能超过6个字符'
  } else {
    errors.nickname = ''
  }
}

// 整体表单验证
const validateForm = () => {
  validateUsername()
  validatePassword()
  validateNickname()
  
  return !errors.username && !errors.password && !errors.nickname
}

// 注册方法
const handleRegister = async () => {
  if (!validateForm()) return
  
  try {
    const response = await registerAPI({
      username: form.username.trim(),
      password: form.password,
      nickname: form.nickname.trim()
    } as RegisterForm)
    
    const res: RegisterResponse = response.data
    if (res.code === 200) {
      ElMessage.success('注册成功')
    } else {
      ElMessage.error(res.msg || '注册失败')
    }
  } catch (error) {
    ElMessage.error('注册失败')
  }finally {
    loading.value = false
  }
}

// 登录方法
const handleLogin = async () => {
  // 验证表单
  if (!validateForm()) return
  
  try {
    loading.value = true
    const response = await loginAPI({
      username: form.username.trim(),
      password: form.password
    } as LoginForm)
    
    const res: LoginResponse = response.data
    if (res.code === 200) {
      // 跳转到主页
      router.push('/home')
    } else {
      ElMessage.error(res.msg || '登录失败')
    }
  } catch (error) {
    ElMessage.error('登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-container">
    <!-- 左侧品牌展示区 -->
    <div class="left-panel">
      <div class="brand-box">
        <h1 class="brand-title">
          <span class="stick">Stick</span>
          <span class="on">On</span>
        </h1>
      </div>
      <div class="welcome-message">
        <p>欢迎回来～在这里开启美好的一天 🌿</p>
      </div>
    </div>
    
    <!-- 右侧登录表单区 -->
    <div class="right-panel">
      <div class="login-card">
        <h2>用户登录</h2>
        <div class="form-group">
          <label class="form-label">用户名</label>
          <input 
            v-model="form.username"
            type="text" 
            class="form-input"
            :class="{ 'error': errors.username }"
            @blur="validateUsername"
            placeholder="请输入用户名"
          />
          <div v-if="errors.username" class="error-message">{{ errors.username }}</div>
        </div>
        
        <div class="form-group">
          <label class="form-label">密码</label>
          <input 
            v-model="form.password"
            type="password" 
            class="form-input"
            :class="{ 'error': errors.password }"
            @blur="validatePassword"
            placeholder="请输入密码"
          />
          <div v-if="errors.password" class="error-message">{{ errors.password }}</div>
        </div>
        
        <div class="form-group">
          <label class="form-label">昵称</label>
          <input 
            v-model="form.nickname"
            type="text" 
            class="form-input"
            :class="{ 'error': errors.nickname }"
            @blur="validateNickname"
            placeholder="请输入昵称"
          />
          <div v-if="errors.nickname" class="error-message">{{ errors.nickname }}</div>
        </div>
        
        <div class="button-group">
          <button 
            @click="handleRegister"
            class="btn register-btn"
            :disabled="loading"
          >
            注册
          </button>
          <button 
            @click="handleLogin"
            class="btn login-btn"
            :disabled="loading"
          >
            <span v-if="loading" class="loading-icon">⟳</span>
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 修改 login-container 样式 */
.login-container {
  display: flex;
  height: 100vh; /* 使用 viewport height 确保高度为全屏 */
  width: 100vw; /* 使用 viewport width 确保宽度为全屏 */
  background: linear-gradient(135deg, #e8f5e9, #81c784);
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  padding: 0;
  margin: 0;
}

/* 修改 body 和 html 样式 */
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  overflow-x: hidden; /* 防止水平滚动条出现 */
}

/* 修改 left-panel 和 right-panel 的样式 */
.left-panel, .right-panel {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }
  
  .left-panel, .right-panel {
    width: 100%; /* 在小屏幕上让左右面板占据整个宽度 */
  }
}

.left-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 2rem;
  color: #2e7d32;
}

.brand-box {
  border: 2px solid #4caf50;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.2);
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.brand-title {
  text-align: center;
  margin: 0;
}

.stick {
  display: block;
  font-size: 7rem;
  font-weight: 900;
  letter-spacing: -2px;
  text-shadow: 3px 3px 6px rgba(0, 0, 0, 0.3);
  color: #1b5e20;
}

.on {
  display: block;
  font-size: 5rem;
  font-weight: 900;
  letter-spacing: -2px;
  text-shadow: 3px 3px 6px rgba(0, 0, 0, 0.3);
  color: #388e3c;
  margin-top: -1rem;
}

.welcome-message {
  background: rgba(255, 255, 255, 0.7);
  color: #1b5e20;
  padding: 1.5rem;
  border-radius: 10px;
  border: 1px solid #a5d6a7;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(5px);
}

.welcome-message p {
  font-size: 1.2rem;
  text-align: center;
  margin: 0;
  font-weight: 500;
}

.right-panel {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

.login-card {
  background: rgba(255, 255, 255, 0.85);
  padding: 2.5rem;
  border-radius: 15px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  width: 100%;
  max-width: 400px;
  transition: transform 0.3s ease;
}

.login-card:hover {
  transform: translateY(-5px);
}

.login-card h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #1b5e20;
  font-size: 1.8rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-label {
  display: inline-block;
  background: #4caf50;
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 4px;
  font-size: 0.9rem;
  margin-bottom: 0.5rem;
}

.form-input {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #a5d6a7;
  border-radius: 4px;
  font-size: 1rem;
  box-sizing: border-box;
  transition: all 0.3s ease;
}

.form-input:focus {
  outline: none;
  border-color: #388e3c;
  box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.2);
}

.form-input.error {
  border-color: #f44336;
}

.error-message {
  color: #f44336;
  font-size: 0.85rem;
  margin-top: 0.3rem;
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 2rem;
}

.btn {
  flex: 1;
  padding: 0.8rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.register-btn {
  background: #c8e6c9;
  color: #2e7d32;
  border: 1px solid #81c784;
  margin-right: 1rem;
}

.register-btn:hover:not(:disabled) {
  background: #a5d6a7;
}

.login-btn {
  background: #4caf50;
  color: white;
  border: 1px solid #388e3c;
  margin-left: 1rem;
}

.login-btn:hover:not(:disabled) {
  background: #388e3c;
}

.loading-icon {
  display: inline-block;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}


</style>