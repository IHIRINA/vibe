import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { initializeCapacitor } from './capacitor'

const app = createApp(App)

// 初始化Capacitor
initializeCapacitor()

// 创建pinia实例并使用持久化插件
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

// 使用路由和状态管理
app.use(router)
app.use(pinia)
app.use(ElementPlus)

app.mount('#app')