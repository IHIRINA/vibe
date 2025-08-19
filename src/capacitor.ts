import { Capacitor } from '@capacitor/core';

export function isNativePlatform(): boolean {
  return Capacitor.isNativePlatform();
}

export function initializeCapacitor() {
  // 在这里可以添加Capacitor插件的初始化代码
  console.log('Capacitor initialized');
  console.log('Platform:', Capacitor.getPlatform());
}