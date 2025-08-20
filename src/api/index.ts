import request from '../utils/request'
import type {
  RegisterForm,
  RegisterResponse,
  LoginForm,
  LoginResponse,
  SayingResponse,
  UserInfoResponse,
  GetTilesParams,
  GetTilesResponse,
  CreateTileForm,
  CreateTileResponse,
  UpdateTileForm,
  UpdateTileResponse,
  GetPersonsResponse,
  UpdatePersonForm,
  UpdatePersonResponse,
  AnalyzePersonResponse,
  UpdatePasswordForm,
  UpdatePasswordResponse,
  AIChatForm,
  AIChatResponse
} from './type'

// 用户注册
export const registerAPI = (data: RegisterForm) => {
  return request.post<RegisterResponse>('/auth/register', data)
}

// 用户登录
export const loginAPI = (data: LoginForm) => {
  return request.post<LoginResponse>('/auth/login', data)
}

// 获取每日一句
export const getSayingAPI = () => {
  return request.get<SayingResponse>('/saying')
}

// 获取当前用户信息
export const getUserInfoAPI = () => {
  return request.get<UserInfoResponse>('/user/profile')
}

// 获取磁贴列表
export const getTilesAPI = (params?: GetTilesParams) => {
  return request.get<GetTilesResponse>('/tiles', { params })
}

// 新增磁贴
export const createTileAPI = (data: CreateTileForm) => {
  return request.post<CreateTileResponse>('/tiles', data)
}

// 编辑磁贴
export const updateTileAPI = (tileId: number, data: UpdateTileForm) => {
  return request.put<UpdateTileResponse>(`/tiles/${tileId}`, data)
}

// 删除磁贴
export const deleteTileAPI = (tileId: number) => {
  return request.delete(`/tiles/${tileId}`)
}

// 获取人物列表（含磁贴）
export const getPersonsAPI = (personId: number) => {
  return request.get<GetPersonsResponse>(`/persons/${personId}`)
}

// 修改人物名称
export const updatePersonAPI = (personId: number, data: UpdatePersonForm) => {
  return request.put<UpdatePersonResponse>(`/persons/${personId}`, data)
}

// AI分析人物事件
export const analyzePersonAPI = (personId: number) => {
  return request.post<AnalyzePersonResponse>(`/persons/${personId}/analyze`)
}

// 修改密码
export const updatePasswordAPI = (data: UpdatePasswordForm) => {
  return request.put<UpdatePasswordResponse>('/user/password', data)
}

// AI聊天
export const aiChatAPI = (data: AIChatForm) => {
  return request.post<AIChatResponse>('/ai/chat', data)
}