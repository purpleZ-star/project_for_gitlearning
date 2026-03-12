<template>
  <el-container style="height: 100vh">
    <el-aside :width="isCollapse ? '64px' : '220px'" style="transition: width 0.3s">
      <div class="logo" :style="{ padding: isCollapse ? '20px 10px' : '20px' }">
        <i class="el-icon-cloudy" style="font-size: 24px; color: #409EFF"></i>
        <span v-show="!isCollapse" style="margin-left: 8px; font-size: 16px; font-weight: bold; color: #fff">
          空气质量监测
        </span>
      </div>
      <el-menu :default-active="$route.path" router background-color="#304156"
        text-color="#bfcbd9" active-text-color="#409EFF" :collapse="isCollapse">
        <el-menu-item index="/dashboard">
          <i class="el-icon-data-board"></i>
          <span slot="title">数据看板</span>
        </el-menu-item>
        <el-menu-item index="/visualization">
          <i class="el-icon-data-analysis"></i>
          <span slot="title">数据可视化</span>
        </el-menu-item>
        <el-menu-item index="/air-quality">
          <i class="el-icon-document"></i>
          <span slot="title">空气质量数据</span>
        </el-menu-item>
        <el-menu-item index="/city">
          <i class="el-icon-office-building"></i>
          <span slot="title">城市管理</span>
        </el-menu-item>
        <el-menu-item index="/station">
          <i class="el-icon-place"></i>
          <span slot="title">监测站点</span>
        </el-menu-item>
        <el-menu-item index="/user" v-if="$store.getters.isAdmin">
          <i class="el-icon-user"></i>
          <span slot="title">用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header>
        <div style="display: flex; align-items: center">
          <i :class="isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'"
            style="font-size: 22px; cursor: pointer" @click="isCollapse = !isCollapse"></i>
          <span style="margin-left: 15px; font-size: 16px">{{ $route.meta.title }}</span>
        </div>
        <div style="display: flex; align-items: center">
          <span style="margin-right: 15px">{{ user && user.realName }}</span>
          <el-button type="text" style="color: #fff" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main style="background: #f0f2f5; overflow-y: auto">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Layout',
  data() {
    return {
      isCollapse: false
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    }
  },
  methods: {
    handleLogout() {
      this.$confirm('确定退出登录？', '提示', { type: 'warning' }).then(() => {
        this.$store.dispatch('logout')
        this.$router.push('/login')
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #263445;
  white-space: nowrap;
  overflow: hidden;
}
</style>
