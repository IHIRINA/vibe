# Stick On 

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

# Stick On - Spring Boot 后端工程

## 项目简介

这是一个基于Spring Boot的后端工程，实现了磁贴编辑、人物管理和AI分析等功能。该工程提供了完整的REST API接口，支持用户注册登录、磁贴管理、人物管理以及AI分析等核心功能。

## 技术栈

- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- MySQL
- Spring AI (OpenAI)
- Lombok

## 项目结构

```
src/main/java/com/example/tileapp/
├── config/          # 配置类
│   └── SecurityConfig.java  # Spring Security配置
├── controller/      # 控制器层
│   ├── AuthController.java  # 用户认证相关接口
│   ├── TileController.java  # 磁贴管理相关接口
│   ├── PersonController.java  # 人物管理相关接口
│   └── AIController.java  # AI相关接口
├── model/           # 实体类
│   ├── User.java    # 用户实体
│   ├── Saying.java  # 语录实体
│   ├── Tile.java    # 磁贴实体
│   ├── Person.java  # 人物实体
│   └── PersonTile.java  # 人物-磁贴关联实体
├── repository/      # 数据访问层
│   ├── UserRepository.java
│   ├── SayingRepository.java
│   ├── TileRepository.java
│   ├── PersonRepository.java
│   └── PersonTileRepository.java
├── service/         # 服务层
│   ├── impl/        # 服务实现
│   │   ├── UserServiceImpl.java
│   │   ├── TileServiceImpl.java
│   │   ├── PersonServiceImpl.java
│   │   └── AIServiceImpl.java
│   ├── UserService.java
│   ├── TileService.java
│   ├── PersonService.java
│   └── AIService.java
└── TileAppApplication.java  # 主应用类
```

## 数据库配置

在`application.properties`文件中配置MySQL数据库连接：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tile_app?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

## OpenAI配置

在`application.properties`文件中配置OpenAI API密钥：

```properties
spring.ai.openai.api-key=your-api-key
spring.ai.openai.chat.model=gpt-3.5-turbo
spring.ai.openai.chat.temperature=0.7
```

## 功能模块

### 1. 用户认证

- 用户注册
- 用户登录
- 密码更新
- 个人资料更新

### 2. 磁贴管理

- 创建磁贴
- 更新磁贴
- 删除磁贴
- 获取用户的所有磁贴
- 获取单个磁贴详情

### 3. 人物管理

- 创建人物
- 更新人物
- 删除人物
- 获取用户的所有人物
- 获取单个人物详情
- 为人物添加磁贴
- 从人物中移除磁贴
- AI分析人物的磁贴内容

### 4. AI聊天

- 提供AI聊天接口

## API接口文档

### 用户认证

- POST `/auth/register` - 用户注册
- POST `/auth/login` - 用户登录

### 磁贴管理

- POST `/tiles` - 创建磁贴
- PUT `/tiles/{tileId}` - 更新磁贴
- DELETE `/tiles/{tileId}` - 删除磁贴
- GET `/tiles?userId={userId}` - 获取用户的所有磁贴
- GET `/tiles/{tileId}` - 获取单个磁贴详情

### 人物管理

- POST `/persons` - 创建人物
- PUT `/persons/{personId}` - 更新人物
- DELETE `/persons/{personId}` - 删除人物
- GET `/persons?userId={userId}` - 获取用户的所有人物
- GET `/persons/{personId}` - 获取单个人物详情
- POST `/persons/{personId}/tiles/{tileId}` - 为人物添加磁贴
- DELETE `/persons/{personId}/tiles/{tileId}` - 从人物中移除磁贴
- GET `/persons/{personId}/analyze` - AI分析人物的磁贴内容

### AI聊天

- POST `/ai/chat` - AI聊天

## 启动项目

使用以下命令启动项目：

```bash
mvn spring-boot:run
```

或者编译打包后运行jar文件：

```bash
mvn clean package
java -jar target/tile-app-0.0.1-SNAPSHOT.jar
```

## 注意事项

1. 确保已安装MySQL数据库并创建了对应的数据库
2. 配置正确的OpenAI API密钥以使用AI功能
3. 项目运行在8080端口，可以通过`http://localhost:8080`访问

# 部署指南
## 一、后端
1. 安装SDK
2. 正确连接数据库，配置openai的key
3. mvn spring-boot:run

## 二、前端 
1. 配置后端url
2. 将 dist 目录上传到 Nginx 静态文件目录    
修改 Nginx 配置         
3. Android 端部署（原生 App）      
- 初始化 Capacitor 环境 ：        
npx cap add android

- 构建前端代码并同步到 Android 工程        
npm run build        
npx cap sync android         
- Android Studio 编译与发布         
打开 Android 工程：      
npx cap open android