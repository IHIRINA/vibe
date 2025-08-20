# 开发流程说明（Stick On）
> 记录使用「通义灵码」在 VS Code 中以 **vibe-coding** 方式完成Stick On项目的全过程。 

## 接口文档设计

### 原始提示词

```
这个项目主要是以磁贴的形式记录人们的生活，在第一个页面中，用户需要进行注册或者登录，然后还会从数据库中调取一句正能量的话话来展示在前端页面，在第二个已经登录进去的页面中，接口文档需要实现，对于用户头像和昵称的调用并展示在前端页面，然后前端从后端发起请求获取数据库中当前存有的磁贴，并按时间顺序展示在前端页面中，此外还可以进行日期的筛选，前端向后端发送日期，开始日期和结束日期筛选日期之间的磁贴，并展示在前端页面中，此外还有一个新增按钮，点完新增按钮后，它会让你输入内容之后点击确定，可以将内容和当前时间等参数发送给后端，后端插入数据库中，然后点击磁贴查看后还可以再点击编辑，点击编辑时则可以进行磁贴的编辑，编辑之后点击应用，可以将编辑后的磁贴发送给后端，后端进行对应数据库的修改。
接着是第三个页面，第三个页面是对之前发送的磁铁AI将磁器的内容归纳到具体的人物中，因此第三个页面主要展示的是每个人对应的磁贴，所以点入第三个页面时，需要前端获取后端已经归纳好的人物数据库，以及其对应的磁贴渲染到前端，在点击人物一的磁贴后，则可以对人物仪进行修改名称的功能，修改名称后则会发送请求到后端对数据库进行修改，然后同步到前端。同时可以点击AI功能按钮来，发送请求到后端，将这个人对应的事件用AI分析之后发送回前端，然后点击这个人物对应的事件磁贴后还可以进行编辑，编辑之后点应用后会将事件发送给后端，进行数据库的更新，并同步到前端。
第4个页面是“我的”页面，我的页面中需要获取头像和昵称的数据以及实现修改密码的功能，其次还需要有一个，AI聊天框的功能在输入栏中输入你想问的问题后再点确定，则将数据发送给后端，AI会发送回数据给前端
```



## 前端

---

### 1. 环境准备

| 步骤 | 操作                           | Prompt（在终端执行）     |
| ---- | ------------------------------ | ------------------------ |
| 1    | 安装 Node ≥18                  | `node -v`                |
| 2    | 全局安装 Vite                  | `npm create vite@latest` |
| 3    | 选择 **Vue + TypeScript** 模板 | 回车确认                 |

---

### 2. 项目初始化 Prompt 序列

在 VS Code 打开项目根目录后，**在通义灵码对话窗口**依次输入以下 Prompt（复制即可）：

```text
#1 安装依赖
npm install vue-router@4 pinia axios element-plus @element-plus/icons-vue
```

```text
#2 生成目录结构
请帮我生成以下目录结构（不存在则创建）：
src/
├─ api/
│  ├─ index.ts
│  └─ type.ts
├─ components/
│  ├─ MagnetAddDialog.vue
│  └─ MagnetEditDialog.vue
├─ pages/
│  └─ MagnetPage.vue
├─ stores/
│  └─ user.ts
├─ utils/
│  └─ request.ts
```

---

### 3. 核心代码生成 Prompt 序列

#### 3.1 请求封装 `utils/request.ts`

```text
请帮我生成 src/utils/request.ts，要求：
- 使用 axios
- baseURL = 'http://localhost:8080'
- 暴露 request 实例
- 响应拦截器：当状态码为 401/402/403/404/405 时分别使用 ElMessage 弹出对应错误提示
```

#### 3.2 类型定义 `api/type.ts`

```text
在 src/api/type.ts 中定义以下类型：
RegisterForm
RegisterResponse
LoginForm
LoginResponse
SayingResponse
userInfoResponse
字段与结构严格按照我提供的 markdown 表格
```

#### 3.3 API 统一导出 `api/index.ts`

```text
在 src/api/index.ts 中：
- 从 type.ts 导入所有类型
- 实现接口：
  - registerAPI
  - loginAPI
  - getSayingAPI
  - getTilesAPI
  - createTileAPI
  - updateTileAPI
  - deleteTileAPI
注意：除 registerAPI 外，其余接口均需在请求头携带
Authorization: Bearer ${token}
```

#### 3.4 状态管理 `stores/user.ts`

```text
创建 Pinia store：useUserStore
- state：id, username, nickname, avatar, token
- actions：login, logout
- 持久化 token 到 localStorage
```

---

### 4. 组件级 Prompt 序列

#### 4.1 MagnetAddDialog.vue

```text
使用 Vue3 <script setup> + ElementPlus 组件，编写 MagnetAddDialog.vue：
- props: visible, initialData
- emit: update:visible, close, save
- 样式要求：绿色渐变标题栏、响应式宽度...
（以下省略，与需求完全一致）
```

#### 4.2 MagnetEditDialog.vue

```text
与 MagnetAddDialog 相似，但：
- 标题动态显示“修改磁贴 X”
- 确认按钮文案“保存修改”
- 取消时不重置表单
```

#### 4.3 MagnetPage.vue

```text
使用 Vue3 <script setup> + ElementPlus + vue-router + pinia 编写完整页面 MagnetPage.vue：
- 左侧可伸缩侧边栏
- 右侧磁贴列表
- 组件挂载即调用 getTilesAPI 并渲染
- 交互逻辑：新增/修改/删除/筛选
- 响应式布局
```

---

### 5. Bug-fix Prompt（迭代）

当第一次生成后出现以下问题，使用对应 Prompt 修复：

| 问题                 | Bug-fix Prompt                                               |
| -------------------- | ------------------------------------------------------------ |
| 请求头未携带 token   | 在 request.ts 的请求拦截器里读取 useUserStore().token 并追加 Authorization |
| 初始磁贴未渲染       | 在 MagnetPage.vue 的 onMounted 中立即调用 `await fetchTiles()` |
| 新增磁贴后未刷新列表 | 在 createTileAPI 成功后再次调用 fetchTiles()                 |
| 修改弹窗未回显数据   | 确保 MagnetEditDialog 的 watch 监听 initialData 并深拷贝     |
| 删除弹窗点击无响应   | 确认 deleteTileAPI 调用后重新拉取列表并更新视图              |

---

### 6. 运行与验证

```text
npm run dev
```

浏览器打开 http://localhost:5173  
检查：  

- 登录/注册正常  

- 磁贴 CRUD 正常  

- 401 等错误弹窗提示正常  

- 响应式布局正常  

### 7.手动调整路由以及样式



## 原始prompt

```
利用vite脚手架新建一个vue3框架的项目，语言用ts，使用vue-router做路由管理，使用pinia做状态管理，使用axios做请求处理，在utils文件夹下封装request请求并暴露
```

```
在vibe/utils文件夹下修改，封装request请求，url是‘http://localhost:8080'，响应拦截器中填写401 402 403 404 405状态码时分别ElMessage出错误。 然后在api文件夹下创立两个文件，type.ts里封装请求体和响应体RegisterForm（ "username": "alice", "password": "123456", "nickname": "Alice", "avatar": "https://example.com/avatar.png"，avatar可选），RegisterResponse（| 名称 | 类型 | 是否必须 | 备注 | | ---- | ---------- | -------- | ----------------------- | | code | number | 必须 | 响应码, 1 成功 ; 0 失败 | | msg | string | 非必须 | 提示信息 | | data | object | 必须 | 返回的数据 | | \ | - id | number | 必须 | | \ | - username | string | 必须 | | \ | - nickname | string | 必须 | | \ | - avatar | string | 必须 | | \ | - token | string | 必须 |） LoginForm（ "username": "alice", "password": "123456"），LoginResponse（| 名称 | 类型 | 是否必须 | 备注 | | ---- | ---------- | -------- | ----------------------- | | code | number | 必须 | 响应码, 1 成功 ; 0 失败 | | msg | string | 非必须 | 提示信息 | | data | object | 必须 | 返回的数据 | | \ | - id | number | 必须 | | \ | - username | string | 必须 | | \ | - nickname | string | 必须 | | \ | - avatar | string | 必须 | | \ | - token | string | 必须 |） SayingResponse（| 名称 | 类型 | 是否必须 | 备注 | | ---- | ------ | -------- | -------- | | code | number | 必须 | 响应码 | | msg | string | 非必须 | 提示信息 | | data | object | 必须 | 返回数据 | | \ | - text | string | 必须 |） userInfoResponse（| 名称 | 类型 | 是否必须 | 备注 | | ---- | ---------- | -------- | ------ | | code | number | 必须 | 响应码 | | msg | string | 非必须 | 提示 | | data | object | 必须 | 数据 | | \ | - nickname | string | 必须 | | \ | - avatar | string | 必须 |）
```

```
在api文件夹下建立index.ts文件，导入type.ts里暴露出的请求体和响应体，在index.ts里面集成接口，下面是接口信息
```

```
请使用 Vue 3 的 <script setup> 语法和 Element Plus 组件库，编写两个功能相似但用途不同的模态对话框组件（在components文件夹下）：MagnetAddDialog.vue 和 MagnetEditDialog.vue。

共同要求（两个组件都需满足） 使用 <el-dialog> 实现，支持 v-model:visible 双向绑定。 弹窗标题栏使用绿色渐变背景（linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%)），文字颜色为 #1b5e20。 弹窗内容区域包含一个居中的表单，最大宽度 600px，带有轻微阴影和圆角。 表单包含三个字段： 磁贴名称：<el-input>，占位符“请输入磁贴名称” 磁贴内容：<el-textarea>，4 行，占位符“请输入磁贴内容” 磁贴颜色：<el-color-picker>，支持预定义颜色 预定义颜色数组为： ['#4caf50', '#66bb6a', '#81c784', '#a5d6a7', '#c8e6c9', '#e8f5e9', '#2e7d32', '#1b5e20'] 弹窗底部有“取消”和“确认”按钮，布局为右对齐。 “取消”按钮样式： 背景色：#f0f9f0 文字色：#2e7d32 边框色：#c8e6c9 悬停时背景变为 #e8f5e9 “确认”按钮使用绿色渐变： 背景：linear-gradient(135deg, #4caf50 0%, #388e3c 100%) 悬停时变为：linear-gradient(135deg, #388e3c 0%, #2e7d32 100%) 弹窗整体背景色设为 #f9fdf9，圆角 12px。 表单输入框默认边框为 #c8e6c9，获得焦点时边框和阴影增强。 弹窗宽度响应式： 屏幕 < 768px：90% < 1200px：70% 其他：50% 使用 defineProps 接收 visible 和 initialData，使用 defineEmits 触发 update:visible、close、save 事件。 使用 watch 监听 initialData 变化并同步到表单。 表单数据初始化为 initialData 的副本。 组件一：MagnetAddDialog.vue（新增磁贴） 标题为：“新增磁贴” 确认按钮文字为：“添加” 点击“取消”或关闭时，重置表单 为默认值： { name: '', content: '', color: '#4caf50' } 点击“添加”后，触发 save 事件并传递表单数据，然后重置表单并关闭弹窗。 使用 custom-class="magnet-edit-dialog" 保持样式统一。 组件二：MagnetEditDialog.vue（修改磁贴） 标题动态显示为：修改磁贴 X，其中 X 是 currentIndex + 1 接收额外的 currentIndex 属性（Number 类型，默认 -1） 确认按钮文字为：“保存修改” 点击“取消”或关闭时，不重置表单，仅关闭弹窗。 点击“保存修改”后，触发 save 事件并传递当前表单数据，然后关闭弹窗。 表单不设默认值，完全依赖 initialData 初始化。 技术栈要求 使用 Vue 3 <script setup> 引入 Element Plus 组件：ElDialog, ElForm, ElFormItem, ElInput, ElTextarea, ElColorPicker, ElButton 输出两个完整的 .vue 文件代码，包含 <template>、<script setup>、<style scoped> 样式模块化，避免全局污染 请分别输出 MagnetAddDialog.vue 和 MagnetEditDialog.vue 的完整代码。
```

```
请使用 Vue 3 + <script setup> 语法和 Element Plus 组件库，编写一个名为 MagnetPage.vue 的完整页面组件。

🧱 页面结构 使用 flex 布局，分为左右两部分： 左侧：可伸缩侧边栏 右侧：主要内容区域 整体背景为绿色渐变：linear-gradient(135deg, #f0f9f0 0%, #e8f5e9 100%) 页面高度占满视口（height: 100vh） 📌 侧边栏（Sidebar） 默认宽度 220px，收起后为 80px，带 0.3s 过渡动画。 包含一个 伸缩按钮（圆形，位于右上角，使用 ArrowLeft / ArrowRight 图标），点击切换展开/收起状态。 展开时显示： 用户头像（<el-avatar>，尺寸 60px，图片 URL：https://cdn.jsdelivr.net/gh/qingruihappy/images@main/avatar.jpg） 昵称“昵称”（深绿色，#1b5e20）：用户登录以后的id，username，nickname等都储存在pinia里了，所以这个昵称就响应式显示pinia里的nickname 菜单项使用 <el-menu>，包含： 磁贴（图标：Magnet，index="/home"） 人们（图标：User，index="/people"） 我（图标：UserFilled，index="/profile"） 菜单项样式： 背景色：rgba(240, 249, 240, 0.9) 文字色：#2e7d32 激活文字色：#1b5e20 悬停背景：浅绿色透明层 圆角 6px 🖼️ 主内容区（Main Content） 包含： 顶部栏：display: flex，左右分布 左侧：一句随机显示的“鸡汤”标语，从api中的getSayingAPI接口获取 样式：绿色渐变背景、圆角、内边距、阴影。 右侧：筛选区域 “筛选”按钮（绿色渐变） 开始日期选择器 “到” 结束日期选择器 新增按钮：一个圆形“+”按钮（使用 Plus 图标），点击后将MagnetAddDialog.vue组件当作弹窗显示，当用户点击确认新增以后向createTileAPI接口中发送请求，请求成功以后显示在页面上，否则ElMessage提示错误 磁贴列表： 初始显示 3 个磁贴，使用 v-for 渲染，数据为带唯一 id 的对象数组。 每个磁贴为正方形（aspect-ratio: 1/1），宽度自适应（PC 每行 4 个，响应式调整）。 背景使用绿色系渐变（提供 8 种不同渐变，循环使用）。 内容居中显示“stick 1”、“stick 2”等。 右上角有一个小删除按钮（Close 图标），点击删除该磁贴。 悬停时有上浮动画（transform: translateY(-5px)）和更强阴影。 底部标识：右下角显示文字 “Stick On”，斜体，深绿色。 ⚙️ 交互逻辑 点击菜单项：使用 vue-router 跳转到对应路径。 点击“筛选”按钮：console.log 当前日期范围。 点击“+”按钮：点击后将MagnetAddDialog.vue组件当作弹窗显示，当用户点击确认新增以后向createTileAPI接口中发送请求，请求成功以后显示在页面上，否则ElMessage提示错误 点击“修改”按钮：点击后将MagnetEditDialog.vue组件当作弹窗显示，当用户点击确认修改以后向updateTileAPI接口中发送请求，请求成功以后显示在页面上，否则ElMessage提示错误 点击删除按钮：点击后出现一个弹窗显示是否确认删除，如果用户点击确认，就向deleteTileAPI发送请求，请求成功就在页面上显示删除，否则也提示错误 页面加载时，使用 onMounted 随机设置一条标语。 🎨 视觉与响应式 所有按钮（如“筛选”、“+”）使用绿色渐变背景（#4caf50 → #388e3c），悬停加深。 磁贴删除按钮为白色半透明圆点，悬停时变明显。 响应式设计： max-width: 1200px：每行 3 个磁贴 max-width: 992px：每行 2 个 max-width: 768px：侧边栏变为横向布局，主内容区宽度 100% max-width: 576px：磁贴全宽显示 📦 技术栈要求 使用 Vue 3 <script setup> 引入 Element Plus 组件：ElMenu, ElMenuItem, ElAvatar, ElButton, ElDatePicker, ElIcon 使用 ref, onMounted, computed（如渐变颜色） 路由使用 vue-router 输出一个完整的 .vue 文件，包含 <template>、<script setup>、<style scoped> 请输出 MagnetPage.vue 的完整代码。
```

```
请使用 Vue 3 + <script setup> 语法和 Element Plus 组件库，编写一个名为 MagnetPage.vue 的完整页面组件。

🧱 页面结构 使用 flex 布局，分为左右两部分： 左侧：可伸缩侧边栏 右侧：主要内容区域 整体背景为绿色渐变：linear-gradient(135deg, #f0f9f0 0%, #e8f5e9 100%) 页面高度占满视口（height: 100vh） 📌 侧边栏（Sidebar） 默认宽度 220px，收起后为 80px，带 0.3s 过渡动画。 包含一个 伸缩按钮（圆形，位于右上角，使用 ArrowLeft / ArrowRight 图标），点击切换展开/收起状态。 展开时显示： 用户头像（<el-avatar>，尺寸 60px，图片 URL：https://cdn.jsdelivr.net/gh/qingruihappy/images@main/avatar.jpg） 昵称“昵称”（深绿色，#1b5e20）：用户登录以后的id，username，nickname等都储存在pinia里了，所以这个昵称就响应式显示pinia里的nickname 菜单项使用 <el-menu>，包含： 磁贴（图标：Magnet，index="/home"） 人们（图标：User，index="/people"） 我（图标：UserFilled，index="/profile"） 菜单项样式： 背景色：rgba(240, 249, 240, 0.9) 文字色：#2e7d32 激活文字色：#1b5e20 悬停背景：浅绿色透明层 圆角 6px 🖼️ 主内容区（Main Content） 包含： 顶部栏：display: flex，左右分布 左侧：一句随机显示的“鸡汤”标语，从api中的getSayingAPI接口获取 样式：绿色渐变背景、圆角、内边距、阴影。 右侧：筛选区域 “筛选”按钮（绿色渐变） 开始日期选择器 “到” 结束日期选择器 新增按钮：一个圆形“+”按钮（使用 Plus 图标），点击后将MagnetAddDialog.vue组件当作弹窗显示，当用户点击确认新增以后向createTileAPI接口中发送请求，请求成功以后显示在页面上，否则ElMessage提示错误 磁贴列表： 初始显示 3 个磁贴，使用 v-for 渲染，数据为带唯一 id 的对象数组。 每个磁贴为正方形（aspect-ratio: 1/1），宽度自适应（PC 每行 4 个，响应式调整）。 背景使用绿色系渐变（提供 8 种不同渐变，循环使用）。 内容居中显示“stick 1”、“stick 2”等。 右上角有一个小删除按钮（Close 图标），点击删除该磁贴。 悬停时有上浮动画（transform: translateY(-5px)）和更强阴影。 底部标识：右下角显示文字 “Stick On”，斜体，深绿色。 ⚙️ 交互逻辑 点击菜单项：使用 vue-router 跳转到对应路径。 点击“筛选”按钮：console.log 当前日期范围。 点击“+”按钮：点击后将MagnetAddDialog.vue组件当作弹窗显示，当用户点击确认新增以后向createTileAPI接口中发送请求，请求成功以后显示在页面上，否则ElMessage提示错误 点击“修改”按钮：点击后将MagnetEditDialog.vue组件当作弹窗显示，当用户点击确认修改以后向updateTileAPI接口中发送请求，请求成功以后显示在页面上，否则ElMessage提示错误 点击删除按钮：点击后出现一个弹窗显示是否确认删除，如果用户点击确认，就向deleteTileAPI发送请求，请求成功就在页面上显示删除，否则也提示错误 页面加载时，使用 onMounted 随机设置一条标语。 🎨 视觉与响应式 所有按钮（如“筛选”、“+”）使用绿色渐变背景（#4caf50 → #388e3c），悬停加深。 磁贴删除按钮为白色半透明圆点，悬停时变明显。 响应式设计： max-width: 1200px：每行 3 个磁贴 max-width: 992px：每行 2 个 max-width: 768px：侧边栏变为横向布局，主内容区宽度 100% max-width: 576px：磁贴全宽显示 📦 技术栈要求 使用 Vue 3 <script setup> 引入 Element Plus 组件：ElMenu, ElMenuItem, ElAvatar, ElButton, ElDatePicker, ElIcon 使用 ref, onMounted, computed（如渐变颜色） 路由使用 vue-router 输出一个完整的 .vue 文件，包含 <template>、<script setup>、<style scoped> 请输出 MagnetPage.vue 的完整代码。
```

```
继续改request，除了注册功能的接口都加上请求头携带 Authorization: Bearer {token
```

```
再改MagnetPage.vue，除了示例的三个磁贴不动，在组件一挂载的时候就向api中的getTilesAPI发请求，并把得到的展示在页面上
```

## 后端

---

### 1. 环境准备

| 步骤 | 操作                      | Prompt（在终端执行）             |
| ---- | ------------------------- | -------------------------------- |
| 1    | 安装 JDK 17+              | `java -version`                  |
| 2    | 安装 Maven 3.9+           | `mvn -v`                         |
| 3    | 安装 MySQL 8.0+           | `mysql -V`                       |
| 4    | 创建数据库                | `CREATE DATABASE tile_app;`      |

---

### 2. 项目初始化 Prompt 序列

在 VS Code 打开项目根目录后，**在通义灵码对话窗口**依次输入以下 Prompt：

```text
#1 生成 Spring Boot 基础结构
请使用 Spring Initializr 生成 Maven 项目：
- Group: com.example
- Artifact: tile-app
- Java: 17
- Dependencies: Spring Web, Spring Security, Spring Data JPA, MySQL Driver, Lombok, Spring AI
```

```text
#2 生成目录结构
请帮我生成以下目录结构：
src/main/java/com/example/tileapp/
├── config/
│  ├─ SecurityConfig.java
│  └─ JwtAuthFilter.java
├── controller/
│  ├─ AuthController.java
│  ├─ TileController.java
│  ├─ PersonController.java
│  └─ AIController.java
├── dto/
│  ├─ RegisterForm.java
│  ├─ LoginForm.java
│  └─ ApiResponse.java
├── model/
│  ├─ User.java
│  ├─ Tile.java
│  ├─ Person.java
│  └─ Saying.java
├── repository/
│  ├─ UserRepository.java
│  ├─ TileRepository.java
│  └─ PersonRepository.java
├── service/
│  ├─ UserService.java
│  ├─ TileService.java
│  ├─ PersonService.java
│  └─ AIService.java
└─ TileAppApplication.java
```

---

### 3. 核心代码生成 Prompt 序列

#### 3.1 实体类 `model/User.java`

```text
请帮我生成 src/main/java/com/example/tileapp/model/User.java：
- 使用 JPA 注解
- 字段：id, username, password, nickname, avatar, createdAt
- 使用 Lombok @Data @Entity
```

#### 3.2 实体类 `model/Tile.java`

```text
请帮我生成 src/main/java/com/example/tileapp/model/Tile.java：
- 关联 User 多对一
- 字段：id, content, user, createdAt
- 使用 Lombok @Data @Entity
```

#### 3.3 实体类 `model/Person.java`

```text
请帮我生成 src/main/java/com/example/tileapp/model/Person.java：
- 关联 User 多对一
- 字段：id, name, description, user, createdAt
- 使用 Lombok @Data @Entity
```

#### 3.4 数据访问层 `repository/UserRepository.java`

```text
请帮我生成 src/main/java/com/example/tileapp/repository/UserRepository.java：
- 继承 JpaRepository<User, Long>
- 方法：Optional<User> findByUsername(String username)
```

#### 3.5 服务层 `service/UserService.java`

```text
请帮我生成 src/main/java/com/example/tileapp/service/UserService.java：
- 注册方法：User register(RegisterForm form)
- 登录方法：String login(LoginForm form)
- 使用 BCrypt 加密密码
- 使用 JWT 生成 token
```

#### 3.6 控制器 `controller/AuthController.java`

```text
请帮我生成 src/main/java/com/example/tileapp/controller/AuthController.java：
- POST /auth/register
- POST /auth/login
- 返回统一响应格式 ApiResponse<T>
```

#### 3.7 JWT 过滤器 `config/JwtAuthFilter.java`

```text
请帮我生成 src/main/java/com/example/tileapp/config/JwtAuthFilter.java：
- 继承 OncePerRequestFilter
- 从 Authorization 头提取 token
- 验证并设置 SecurityContext
```

---

### 4. 配置文件 Prompt 序列

#### 4.1 `application.properties`

```text
在 src/main/resources/application.properties 中配置：
- server.port=8080
- spring.datasource.url=jdbc:mysql://localhost:3306/tile_app?useSSL=false&serverTimezone=UTC
- spring.datasource.username=root
- spring.datasource.password=password
- spring.jpa.hibernate.ddl-auto=update
- spring.ai.openai.api-key=your-api-key
```

---

### 5. 功能级 Prompt 序列

#### 5.1 磁贴 CRUD

```text
在 TileController 中实现：
- POST /tiles - 创建磁贴
- PUT /tiles/{id} - 更新磁贴
- DELETE /tiles/{id} - 删除磁贴
- GET /tiles?userId=1 - 获取用户磁贴
- GET /tiles/{id} - 获取磁贴详情
```

#### 5.2 人物管理

```text
在 PersonController 中实现：
- POST /persons - 创建人物
- PUT /persons/{id} - 更新人物
- DELETE /persons/{id} - 删除人物
- GET /persons?userId=1 - 获取用户人物
- GET /persons/{id} - 获取人物详情
- POST /persons/{personId}/tiles/{tileId} - 关联磁贴
- DELETE /persons/{personId}/tiles/{tileId} - 解除关联
```

#### 5.3 AI 分析

```text
在 AIController 中实现：
- GET /persons/{personId}/analyze - 使用 OpenAI 分析人物磁贴内容
- POST /ai/chat - AI 聊天接口
```

---

### 6. Bug-fix Prompt

当第一次生成后出现以下问题，使用对应 Prompt 修复：

| 问题           | Bug-fix Prompt                                               |
| -------------- | ------------------------------------------------------------ |
| 401 未认证     | 在 SecurityConfig 中放行 /auth/**，其余需要认证              |
| 实体字段缺失   | 检查所有实体类是否包含 createdAt 字段，并添加 @CreationTimestamp |
| 外键懒加载异常 | 在关联字段上添加 @JsonIgnoreProperties("user")               |
| 时间格式错误   | 在 application.properties 添加 spring.jackson.time-zone=UTC  |
| AI 接口返回空  | 检查 OpenAI API key 是否正确，并捕获 RestClientException 返回提示 |

---

### 7. 运行与验证

```text
mvn spring-boot:run
```

浏览器或 Postman 访问 http://localhost:8080  
检查：  

- 注册/登录正常  
- 磁贴 CRUD 正常  
- 人物管理正常  
- AI 分析正常  
- 401 等错误返回统一格式