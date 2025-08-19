<template>
  <el-dialog
    :model-value="visible"
    @update:model-value="$emit('update:visible', $event)"
    title="新增磁贴"
    :width="dialogWidth"
    :before-close="handleClose"
    custom-class="magnet-edit-dialog"
  >
    <div class="dialog-content">
      <el-form :model="form" label-width="80px" class="magnet-form">
        <el-form-item label="磁贴名称" prop="name" :rules="[{ required: true, message: '请输入磁贴名称', trigger: 'blur' }]">
          <el-input v-model="form.name" placeholder="请输入磁贴名称"></el-input>
        </el-form-item>
        <el-form-item label="磁贴内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="4"
            placeholder="请输入磁贴内容"
          ></el-input>
        </el-form-item>
        <el-form-item label="磁贴颜色">
          <el-color-picker
            v-model="form.color"
            :predefine="predefinedColors"
            placeholder="选择颜色"
          ></el-color-picker>
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleCancel" class="cancel-btn">取消</el-button>
        <el-button type="primary" @click="handleSave" class="green-btn">添加</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, defineProps, defineEmits, watch } from 'vue';
import { ElForm, ElFormItem, ElInput, ElColorPicker, ElButton, ElDialog } from 'element-plus';

// 定义组件属性
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  // 初始数据，用于预设默认值
  initialData: {
    type: Object,
    default: () => ({
      name: '',
      content: '',
      color: '#4caf50' // 默认使用第一个绿色
    })
  }
});

// 定义组件事件
const emit = defineEmits(['update:visible', 'close', 'save']);

// 表单数据
const form = ref({ ...props.initialData });

// 预定义颜色（与修改组件保持一致）
const predefinedColors = [
  '#4caf50', '#66bb6a', '#81c784', '#a5d6a7',
  '#c8e6c9', '#e8f5e9', '#2e7d32', '#1b5e20'
];

// 弹窗宽度计算（与修改组件保持一致）
const dialogWidth = computed(() => {
  if (window.innerWidth < 768) {
    return '90%';
  } else if (window.innerWidth < 1200) {
    return '70%';
  }
  return '50%';
});

// 监听初始数据变化
watch(() => props.initialData, (newVal) => {
  form.value = { ...newVal };
}, { deep: true });

// 关闭弹窗
const handleClose = () => {
  resetForm();
  emit('close');
  emit('update:visible', false);
};

// 取消操作
const handleCancel = () => {
  resetForm();
  emit('close');
  emit('update:visible', false);
};

// 保存新增
const handleSave = () => {
  emit('save', form.value);
  resetForm(); // 保存后重置表单
  emit('update:visible', false);
};

// 重置表单
const resetForm = () => {
  form.value = {
    name: '',
    content: '',
    color: '#4caf50'
  };
};
</script>

<style scoped>
/* 弹窗样式与修改组件完全一致 */
.magnet-edit-dialog {
  --el-dialog-bg-color: #f9fdf9;
  --el-dialog-border-radius: 12px;
}

.magnet-edit-dialog .el-dialog__header {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  padding: 16px 24px;
  border-radius: 12px 12px 0 0;
}

.magnet-edit-dialog .el-dialog__title {
  color: #1b5e20;
  font-weight: 600;
  font-size: 18px;
}

.magnet-edit-dialog .el-dialog__body {
  padding: 24px;
}

.dialog-content {
  background-color: #fff;
  border-radius: 8px;
  padding: 10px;
  box-shadow: 0 2px 10px rgba(76, 175, 80, 0.05);
}

.magnet-form {
  max-width: 600px;
  margin: 0 auto;
}

.magnet-form .el-form-item {
  margin-bottom: 20px;
}

.magnet-form .el-input,
.magnet-form .el-textarea {
  border-color: #c8e6c9;
  transition: all 0.3s ease;
}

.magnet-form .el-input:focus-within,
.magnet-form .el-textarea:focus-within {
  border-color: #81c784;
  box-shadow: 0 0 0 2px rgba(129, 199, 132, 0.2);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding-top: 10px;
}

.cancel-btn {
  background-color: #f0f9f0;
  color: #2e7d32;
  border-color: #c8e6c9;
}

.cancel-btn:hover {
  background-color: #e8f5e9;
  border-color: #a5d6a7;
}

.green-btn {
  background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%) !important;
  border: none !important;
}

.green-btn:hover {
  background: linear-gradient(135deg, #388e3c 0%, #2e7d32 100%) !important;
}
</style>