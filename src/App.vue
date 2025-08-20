<script setup lang="ts">

</script>

<template>
  <RouterView v-slot="{ Component }">
    <keep-alive>
      <component :is="Component" :key="$route.fullPath" />
    </keep-alive>
  </RouterView>
</template>

<style scoped>

</style>
