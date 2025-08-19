import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

// 用户信息类型
interface UserInfo {
  id: number
  username: string
  nickname: string
  avatar: string
  token: string
}

// 创建一个示例store
export const useMainStore = defineStore('main', () => {
  // 状态
  const count = ref(0)
  const name = ref('Vite + Vue3 + TypeScript')
  const userInfo = ref<UserInfo | null>(null)

  // 计算属性
  const doubleCount = computed(() => count.value * 2)
  const userAvatar = computed(() => {
    return userInfo.value ? userInfo.value.avatar : 'https://cdn.jsdelivr.net/gh/qingruihappy/images@main/avatar.jpg'
  })
  const nickname = computed(() => {
    return userInfo.value ? userInfo.value.nickname : '未设置昵称'
  })

  // 方法
  const increment = () => {
    count.value++
  }

  const updateName = (newName: string) => {
    name.value = newName
  }

  const updateUserInfo = (newUserInfo: UserInfo | null) => {
    userInfo.value = newUserInfo
  }

  const updateNickname = (nickname: string) => {
    if (userInfo.value) {
      userInfo.value.nickname = nickname
    }
  }

  const updateAvatar = (avatar: string) => {
    if (userInfo.value) {
      userInfo.value.avatar = avatar
    }
  }

  return {
    // 状态
    count,
    name,
    userInfo,

    // 计算属性
    doubleCount,
    userAvatar,
    nickname,

    // 方法
    increment,
    updateName,
    updateUserInfo,
    updateNickname,
    updateAvatar
  }
}, {
  persist: true
})