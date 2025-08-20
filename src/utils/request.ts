import axios, { AxiosError } from 'axios'
import { ElMessage } from 'element-plus'
import { useMainStore } from '../store'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080', // 基础URL，设置为 http://localhost:8080
  timeout: 10000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 在发送请求之前做些什么
    // 除了注册接口外，其他接口都需要添加token到请求头
/*     if (!config.url?.includes('/auth/register')) {
      const store = useMainStore()
      if (store.userInfo?.token) {
        config.headers.Authorization = `Bearer ${store.userInfo.token}`
      }
    } */
    return config
  },
  (error: AxiosError) => {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    // 对响应数据做点什么
    return response
  },
  (error: AxiosError) => {
    // 对响应错误做点什么
    const status = error.response?.status
    switch (status) {
      case 401:
        ElMessage.error('未授权，请重新登录')
        break
      case 402:
        ElMessage.error('支付失败')
        break
      case 403:
        ElMessage.error('禁止访问')
        break
      case 404:
        ElMessage.error('请求资源不存在')
        break
      case 405:
        ElMessage.error('请求方法不允许')
        break
      default:
        ElMessage.error('请求失败')
    }
    
    return Promise.reject(error)
  }
)

export default request