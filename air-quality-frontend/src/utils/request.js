import axios from 'axios'
import { Message } from 'element-ui'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000
})

request.interceptors.request.use(
  config => {
    const user = JSON.parse(localStorage.getItem('airQualityUser') || '{}')
    if (user.id) {
      config.headers['X-User-Id'] = user.id
    }
    return config
  },
  error => Promise.reject(error)
)

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      Message.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message))
    }
    return res
  },
  error => {
    Message.error(error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default request
