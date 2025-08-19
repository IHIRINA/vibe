## Stick On 

## —— 把生活“贴”在磁贴上，让记忆有条理，让关系更清晰

## 项目创意[题目三 智享生活]

用磁贴写日记、用 AI 回忆人，一款帮你“把琐碎生活整理成故事”的轻量级记录工具。

#### 解决的核心痛点  

- 生活碎片太多，找不到也记不清；  
- 社交关系混杂，想不起曾经的点滴；  
- 传统日记太“重”，需要更轻更快的记录方式。  

因此，StickOn 用磁贴 + AI 把“记录—整理—回顾”三步压缩成秒级操作，让每个人都能轻松拥有自己的生活剧本，同时让AI为生活赋能。

### 产品功能  

1. 磁贴时间轴  
   登录即见每日一句正能量 + 按时间倒序排列的生活磁贴，支持起止日期快速筛选，回溯过去像翻书一样简单。  
2. 零门槛记录  
   一键“新增”，输入即可轻松保存；，随时点贴再编辑，内容、时间、心情即刻更新。  
3. AI 人物归集  
   AI自动把磁贴聚类到“人物”，第三页呈现“谁与我的故事”：  
   • 改人物昵称、手动微调事件  
   • 点击“AI 分析”，10 秒生成你与 TA 的专属关系小结  
4. 我的空间  
   头像/昵称随时换、密码一键改；内置 AI 聊天框，外挂你的磁贴记忆，根据你的记忆为你解答，陪伴着你。
   
# Stick On - Vue3 + Capacitor 前端工程

## 项目简介

Stick On是一个基于 Vue3 + Capacitor 的跨平台（Web / iOS / Android）前端应用，聚焦磁贴主题，提供现代化的 UI 交互与多端一致体验。项目通过 Pinia 做全局状态管理、Element Plus 构建桌面级组件，并通过 Capacitor 桥接原生能力，实现一次编码多端运行。

## 技术栈

- Vue 3.5（Composition API）
- TypeScript 5.8
- Vite 7.1（构建工具）
- Vue Router 4.5（路由）
- Pinia 3.0（状态管理）
- pinia-plugin-persistedstate 4.5（持久化）
- Element Plus 2.10（UI 组件库）
- Sass-Embedded 1.90（样式预处理器）
- Capacitor 7.4（跨平台运行时）
  - @capacitor/core
  - @capacitor/cli
  - @capacitor/ios
  - @capacitor/android
- Axios 1.11（HTTP 客户端）

## 项目结构

```
src/
├── api/                 # 接口封装层
│   ├── auth.ts
│   ├── tile.ts
│   ├── person.ts
│   └── ai.ts
├── assets/              # 静态资源
│   ├── images/
│   ├── icons/
│   └── styles/
│       └── global.scss
├── components/          # 通用业务组件
│   ├── VibeHeader.vue
│   ├── VibeSidebar.vue
│   └── VibeCard.vue
├── composables/         # 组合式函数
│   ├── useAuth.ts
│   ├── useTile.ts
│   └── usePerson.ts
├── layouts/             # 布局组件
│   └── DefaultLayout.vue
├── pages/               # 页面级组件
│   ├── LoginPage.vue
│   ├── DashboardPage.vue
│   ├── TilesPage.vue
│   ├── PersonsPage.vue
│   └── ChatPage.vue
├── router/              # 路由配置
│   └── index.ts
├── stores/              # Pinia 状态
│   ├── auth.store.ts
│   ├── tile.store.ts
│   └── person.store.ts
├── utils/               # 工具函数
│   ├── request.ts       # axios 实例
│   └── capacitor.ts     # Capacitor 初始化
├── App.vue              # 根组件
└── main.ts              # 应用入口
capacitor.config.ts      # Capacitor 配置
vite.config.ts           # Vite 配置
```

## 环境变量

项目支持 `.env` 系列文件配置，常用变量示例：

```bash
# .env
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_NAME=Vibe
```

## Capacitor 配置

`capacitor.config.ts`

```ts
import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.vibe.app',
  appName: 'Vibe',
  webDir: 'dist',
  bundledWebRuntime: false,
  // iOS
  ios: {
    scheme: 'Vibe',
  },
  // Android
  android: {
    allowMixedContent: true,
  },
};

export default config;
```

## 功能模块

1. 用户认证
   - 注册 / 登录 / 登出
   - Token 持久化
2. 磁贴（Tile）管理
   - 创建 / 编辑 / 删除
   - 列表与详情
3. 人物（Person）管理
   - 创建 / 编辑 / 删除
   - 磁贴关联 / 解绑
   - AI 分析报告
4. AI 聊天
   - 实时对话
   - 流式响应

## 接口约定

> 与后端保持 RESTful 风格一致，前缀统一为 `VITE_API_BASE_URL`

- 认证
  - POST `/auth/register`
  - POST `/auth/login`
- 磁贴
  - POST `/tiles`
  - PUT  `/tiles/:id`
  - DELETE `/tiles/:id`
  - GET `/tiles?userId=:userId`
  - GET `/tiles/:id`
- 人物
  - POST `/persons`
  - PUT `/persons/:id`
  - DELETE `/persons/:id`
  - GET `/persons?userId=:userId`
  - GET `/persons/:id`
  - POST `/persons/:personId/tiles/:tileId`
  - DELETE `/persons/:personId/tiles/:tileId`
  - GET `/persons/:personId/analyze`
- AI 聊天
  - POST `/ai/chat`

## 本地开发

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建 Web 产物
npm run build

# 预览构建结果
npm run preview
```

## 移动端构建

```bash
# 添加平台
npx cap add ios
npx cap add android

# 构建后同步
npm run build
npx cap sync ios
npx cap sync android

# 打开原生 IDE
npx cap open ios
npx cap open android
```

## 注意事项

1. 确保已全局安装 Capacitor CLI  
   `npm i -g @capacitor/cli`
2. 移动端需安装对应平台工具链（Xcode / Android Studio）。
3. 如需使用原生插件，按官方文档在 iOS/Android 工程中手动配置权限与依赖。
4. 项目默认运行在 `http://localhost:5173`（Vite 默认端口）。

### 运行/部署指南

前后端均打包成可执行文件，点击执行后访问xx网站即可