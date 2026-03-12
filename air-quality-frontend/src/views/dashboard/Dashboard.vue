<template>
  <div class="page-container">
    <!-- 统计卡片 -->
    <el-row :gutter="15" style="margin-bottom: 15px">
      <el-col :span="6">
        <div class="stat-card blue">
          <div class="stat-label">监测城市</div>
          <div class="stat-value">{{ stats.cityCount || 0 }}</div>
          <div class="stat-label">个</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card green">
          <div class="stat-label">监测站点</div>
          <div class="stat-value">{{ stats.stationCount || 0 }}</div>
          <div class="stat-label">个</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-label">监测数据总量</div>
          <div class="stat-value">{{ stats.dataCount || 0 }}</div>
          <div class="stat-label">条</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card orange">
          <div class="stat-label">今日新增数据</div>
          <div class="stat-value">{{ stats.todayCount || 0 }}</div>
          <div class="stat-label">条</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="15">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">各城市最新AQI指数</div>
          <div ref="aqiBarChart" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">空气质量等级分布</div>
          <div ref="levelPieChart" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="15">
      <el-col :span="24">
        <el-card class="chart-card">
          <div slot="header">各城市平均AQI排名 TOP10</div>
          <div ref="rankChart" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最新数据表格 -->
    <el-card class="chart-card">
      <div slot="header">各城市最新空气质量</div>
      <el-table :data="latestData" border stripe size="small" max-height="400">
        <el-table-column prop="cityName" label="城市" width="100" />
        <el-table-column prop="aqi" label="AQI" width="80">
          <template slot-scope="scope">
            <span :style="{ color: getAqiColor(scope.row.aqi), fontWeight: 'bold' }">
              {{ scope.row.aqi }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="qualityLevel" label="质量等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getLevelType(scope.row.qualityLevel)" size="small">
              {{ scope.row.qualityLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="pm25" label="PM2.5" width="80" />
        <el-table-column prop="pm10" label="PM10" width="80" />
        <el-table-column prop="so2" label="SO2" width="80" />
        <el-table-column prop="no2" label="NO2" width="80" />
        <el-table-column prop="co" label="CO" width="80" />
        <el-table-column prop="o3" label="O3" width="80" />
        <el-table-column prop="temperature" label="温度(℃)" width="90" />
        <el-table-column prop="humidity" label="湿度(%)" width="90" />
        <el-table-column prop="monitorTime" label="监测时间" min-width="160" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDashboardStats, getLatestByCity, getLevelDistribution, getCityRank } from '@/api'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {},
      latestData: [],
      charts: []
    }
  },
  mounted() {
    this.loadData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.charts.forEach(c => c.dispose())
  },
  methods: {
    async loadData() {
      try {
        const [statsRes, latestRes, levelRes, rankRes] = await Promise.all([
          getDashboardStats(),
          getLatestByCity(),
          getLevelDistribution(),
          getCityRank(10)
        ])
        this.stats = statsRes.data || {}
        this.latestData = latestRes.data || []
        this.initAqiBarChart(this.latestData)
        this.initLevelPieChart(levelRes.data || [])
        this.initRankChart(rankRes.data || [])
      } catch (e) {
        console.error(e)
      }
    },
    initAqiBarChart(data) {
      const chart = echarts.init(this.$refs.aqiBarChart)
      this.charts.push(chart)
      const cities = data.map(d => d.cityName)
      const aqiValues = data.map(d => ({
        value: d.aqi,
        itemStyle: { color: this.getAqiColor(d.aqi) }
      }))
      chart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '10%', containLabel: true },
        xAxis: {
          type: 'category',
          data: cities,
          axisLabel: { rotate: 45, fontSize: 11 }
        },
        yAxis: { type: 'value', name: 'AQI' },
        series: [{
          type: 'bar',
          data: aqiValues,
          barMaxWidth: 40,
          label: { show: true, position: 'top', fontSize: 10 }
        }]
      })
    },
    initLevelPieChart(data) {
      const chart = echarts.init(this.$refs.levelPieChart)
      this.charts.push(chart)
      const colorMap = {
        '优': '#00e400', '良': '#ffff00', '轻度污染': '#ff7e00',
        '中度污染': '#ff0000', '重度污染': '#99004c', '严重污染': '#7e0023'
      }
      chart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { orient: 'vertical', left: 'left', top: 'center' },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          avoidLabelOverlap: true,
          itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
          label: { show: true, formatter: '{b}\n{d}%' },
          data: data.map(d => ({
            name: d.name,
            value: d.value,
            itemStyle: { color: colorMap[d.name] || '#999' }
          }))
        }]
      })
    },
    initRankChart(data) {
      const chart = echarts.init(this.$refs.rankChart)
      this.charts.push(chart)
      const cities = data.map(d => d.cityName).reverse()
      const values = data.map(d => d.avgAqi).reverse()
      chart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '10%', bottom: '3%', containLabel: true },
        xAxis: { type: 'value', name: '平均AQI' },
        yAxis: { type: 'category', data: cities },
        series: [{
          type: 'bar',
          data: values.map(v => ({
            value: v,
            itemStyle: { color: this.getAqiColor(v) }
          })),
          barMaxWidth: 25,
          label: { show: true, position: 'right' }
        }]
      })
    },
    getAqiColor(aqi) {
      if (aqi <= 50) return '#00e400'
      if (aqi <= 100) return '#ffff00'
      if (aqi <= 150) return '#ff7e00'
      if (aqi <= 200) return '#ff0000'
      if (aqi <= 300) return '#99004c'
      return '#7e0023'
    },
    getLevelType(level) {
      const map = { '优': 'success', '良': '', '轻度污染': 'warning', '中度污染': 'danger', '重度污染': 'danger', '严重污染': 'danger' }
      return map[level] || 'info'
    },
    handleResize() {
      this.charts.forEach(c => c.resize())
    }
  }
}
</script>
