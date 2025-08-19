// 用户注册请求体
export interface RegisterForm {
  username: string
  password: string
  nickname: string
  avatar?: string
}

// 用户注册响应体
export interface RegisterResponse {
  code: number
  msg?: string
  data: {
    id: number
    username: string
    nickname: string
    avatar: string
    token: string
  }
}

// 用户登录请求体
export interface LoginForm {
  username: string
  password: string
}

// 用户登录响应体
export interface LoginResponse {
  code: number
  msg?: string
  data: {
    id: number
    username: string
    nickname: string
    avatar: string
    token: string
  }
}

// 说话响应体
export interface SayingResponse {
  code: number
  msg?: string
  data: {
    text: string
  }
}

// 用户信息响应体
export interface UserInfoResponse {
  code: number
  msg?: string
  data: {
    nickname: string
    avatar: string
  }
}

// 获取磁贴列表请求参数
export interface GetTilesParams {
  start_date?: string
  end_date?: string
}

// 磁贴数据
export interface Tile {
  id: number
  content: string
  created_at: string
  updated_at: string
}

// 获取磁贴列表响应体
export interface GetTilesResponse {
  code: number
  msg?: string
  data: Tile[]
}

// 新增磁贴请求体
export interface CreateTileForm {
  content: string
}

// 新增磁贴响应体
export interface CreateTileResponse {
  code: number
  msg?: string
  data: {
    id: number
    content: string
    created_at: string
  }
}

// 编辑磁贴请求体
export interface UpdateTileForm {
  content: string
}

// 编辑磁贴响应体
export interface UpdateTileResponse {
  code: number
  msg?: string
  data: {
    id: number
    content: string
    updated_at: string
  }
}

// 人物磁贴
export interface PersonTile {
  id: number
  content: string
  created_at: string
}

// 人物数据
export interface Person {
  id: number
  name: string
  tiles: PersonTile[]
}

// 获取人物列表响应体
export interface GetPersonsResponse {
  code: number
  msg?: string
  data: Person[]
}

// 修改人物名称请求体
export interface UpdatePersonForm {
  name: string
}

// 修改人物名称响应体
export interface UpdatePersonResponse {
  code: number
  msg?: string
  data: {
    id: number
    name: string
  }
}

// AI分析人物事件响应体
export interface AnalyzePersonResponse {
  code: number
  msg?: string
  data: {
    summary: string
  }
}

// 修改密码请求体
export interface UpdatePasswordForm {
  oldPassword: string
  newPassword: string
}

// 修改密码响应体
export interface UpdatePasswordResponse {
  code: number
  msg?: string
}

// AI聊天请求体
export interface AIChatForm {
  content: string
}

// AI聊天响应体
export interface AIChatResponse {
  code: number
  msg?: string
  data: {
    reply: string
  }
}