import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Dashboard.vue'),
        meta: { title: '数据看板' }
      },
      {
        path: 'air-quality',
        name: 'AirQuality',
        component: () => import('@/views/airquality/AirQualityList.vue'),
        meta: { title: '空气质量数据' }
      },
      {
        path: 'city',
        name: 'City',
        component: () => import('@/views/city/CityList.vue'),
        meta: { title: '城市管理' }
      },
      {
        path: 'station',
        name: 'Station',
        component: () => import('@/views/station/StationList.vue'),
        meta: { title: '监测站点' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/UserList.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'visualization',
        name: 'Visualization',
        component: () => import('@/views/dashboard/Visualization.vue'),
        meta: { title: '数据可视化' }
      }
    ]
  }
]

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
  const user = localStorage.getItem('airQualityUser')
  if (to.path !== '/login' && !user) {
    next('/login')
  } else {
    next()
  }
})

export default router
