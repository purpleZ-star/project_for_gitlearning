import request from '@/utils/request'

// 用户相关
export const login = (data) => request.post('/user/login', data)
export const getUserList = () => request.get('/user/list')
export const saveUser = (data) => request.post('/user', data)
export const updateUser = (data) => request.put('/user', data)
export const deleteUser = (id) => request.delete(`/user/${id}`)

// 城市相关
export const getCityList = (keyword) => request.get('/city/list', { params: { keyword } })
export const saveCity = (data) => request.post('/city', data)
export const updateCity = (data) => request.put('/city', data)
export const deleteCity = (id) => request.delete(`/city/${id}`)

// 监测站点相关
export const getStationList = (keyword) => request.get('/station/list', { params: { keyword } })
export const saveStation = (data) => request.post('/station', data)
export const updateStation = (data) => request.put('/station', data)
export const deleteStation = (id) => request.delete(`/station/${id}`)

// 空气质量数据相关
export const getAirQualityList = (params) => request.get('/air-quality/list', { params })
export const saveAirQuality = (data) => request.post('/air-quality', data)
export const updateAirQuality = (data) => request.put('/air-quality', data)
export const deleteAirQuality = (id) => request.delete(`/air-quality/${id}`)

// 统计分析
export const getLatestByCity = () => request.get('/air-quality/latest')
export const getTrendData = (cityId, days) => request.get('/air-quality/trend', { params: { cityId, days } })
export const getLevelDistribution = () => request.get('/air-quality/level-distribution')
export const getCityRank = (limit) => request.get('/air-quality/rank', { params: { limit } })
export const getDashboardStats = () => request.get('/air-quality/dashboard')
export const getAvgPollutants = () => request.get('/air-quality/avg-pollutants')
