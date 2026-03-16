<template>
  <div class="page-container">
    <!-- 城市选择和时间范围 -->
    <el-card class="chart-card">
      <div slot="header">趋势分析</div>
      <div class="search-bar">
        <el-select v-model="selectedCity" placeholder="选择城市" @change="loadTrend" filterable>
          <el-option v-for="c in cityList" :key="c.id" :label="c.cityName" :value="c.id" />
        </el-select>
        <el-radio-group v-model="days" @change="loadTrend" size="small">
          <el-radio-button :label="7">近7天</el-radio-button>
          <el-radio-button :label="15">近15天</el-radio-button>
          <el-radio-button :label="30">近30天</el-radio-button>
        </el-radio-group>
      </div>
      <div ref="trendChart" style="height: 400px"></div>
    </el-card>

    <el-row :gutter="15">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">各污染物平均浓度雷达图</div>
          <div ref="radarChart" style="height: 380px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">各城市AQI对比散点图</div>
          <div ref="scatterChart" style="height: 380px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="chart-card">
      <div slot="header">污染物浓度堆叠面积图</div>
      <div ref="stackAreaChart" style="height: 400px"></div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getCityList, getTrendData, getAvgPollutants, getLatestByCity } from '@/api'

export default {
  name: 'Visualization',
  data() {
    return {
      cityList: [],
      selectedCity: null,
      days: 7,
      trendData: [],
      charts: []
    }
  },
  async mounted() {
    const res = await getCityList()
    this.cityList = res.data || []
    if (this.cityList.length > 0) {
      this.selectedCity = this.cityList[0].id
    }
    await this.loadAll()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.charts.forEach(c => c.dispose())
  },
  methods: {
    async loadAll() {
      await Promise.all([
        this.loadTrend(),
        this.loadRadar(),
        this.loadScatter()
      ])
    },
    async loadTrend() {
      if (!this.selectedCity) return
      const res = await getTrendData(this.selectedCity, this.days)
      this.trendData = res.data || []
      this.initTrendChart()
      this.initStackAreaChart()
    },
    initTrendChart() {
      let chart = this.charts.find(c => c._dom === this.$refs.trendChart)
      if (!chart) {
        chart = echarts.init(this.$refs.trendChart)
        this.charts.push(chart)
      }
      const times = this.trendData.map(d => d.monitorTime)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['AQI', 'PM2.5', 'PM10'] },
        grid: { left: '3%', right: '4%', bottom: '10%', containLabel: true },
        xAxis: { type: 'category', data: times, axisLabel: { rotate: 30, fontSize: 10 } },
        yAxis: { type: 'value' },
        dataZoom: [{ type: 'inside' }, { type: 'slider' }],
        series: [
          {
            name: 'AQI', type: 'line', smooth: true,
            data: this.trendData.map(d => d.aqi),
            lineStyle: { width: 2 },
            areaStyle: { opacity: 0.1 }
          },
          {
            name: 'PM2.5', type: 'line', smooth: true,
            data: this.trendData.map(d => d.pm25),
            lineStyle: { width: 2 }
          },
          {
            name: 'PM10', type: 'line', smooth: true,
            data: this.trendData.map(d => d.pm10),
            lineStyle: { width: 2 }
          }
        ]
      }, true)
    },
    async loadRadar() {
      const res = await getAvgPollutants()
      const data = res.data || {}
      const chart = echarts.init(this.$refs.radarChart)
      this.charts.push(chart)
      chart.setOption({
        tooltip: {},
        radar: {
          indicator: [
            { name: 'PM2.5', max: 200 },
            { name: 'PM10', max: 300 },
            { name: 'SO2', max: 100 },
            { name: 'NO2', max: 150 },
            { name: 'CO', max: 5 },
            { name: 'O3', max: 300 }
          ],
          shape: 'circle'
        },
        series: [{
          type: 'radar',
          data: [{
            value: [data.avgPm25, data.avgPm10, data.avgSo2, data.avgNo2, data.avgCo, data.avgO3],
            name: '平均浓度',
            areaStyle: { opacity: 0.3 }
          }]
        }]
      })
    },
    async loadScatter() {
      const res = await getLatestByCity()
      const data = res.data || []
      const chart = echarts.init(this.$refs.scatterChart)
      this.charts.push(chart)
      chart.setOption({
        tooltip: {
          formatter: (p) => `${p.data[2]}<br/>PM2.5: ${p.data[0]}<br/>AQI: ${p.data[1]}`
        },
        xAxis: { name: 'PM2.5', type: 'value' },
        yAxis: { name: 'AQI', type: 'value' },
        series: [{
          type: 'scatter',
          symbolSize: (val) => Math.max(val[1] / 10, 8),
          data: data.map(d => [d.pm25, d.aqi, d.cityName]),
          itemStyle: {
            color: (params) => this.getAqiColor(params.data[1])
          },
          label: { show: true, formatter: (p) => p.data[2], position: 'top', fontSize: 10 }
        }]
      })
    },
    initStackAreaChart() {
      let chart = this.charts.find(c => c._dom === this.$refs.stackAreaChart)
      if (!chart) {
        chart = echarts.init(this.$refs.stackAreaChart)
        this.charts.push(chart)
      }
      const times = this.trendData.map(d => d.monitorTime)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['PM2.5', 'PM10', 'SO2', 'NO2', 'O3'] },
        grid: { left: '3%', right: '4%', bottom: '10%', containLabel: true },
        xAxis: { type: 'category', data: times, axisLabel: { rotate: 30, fontSize: 10 } },
        yAxis: { type: 'value', name: '浓度(μg/m³)' },
        dataZoom: [{ type: 'inside' }, { type: 'slider' }],
        series: ['pm25', 'pm10', 'so2', 'no2', 'o3'].map((key, i) => ({
          name: ['PM2.5', 'PM10', 'SO2', 'NO2', 'O3'][i],
          type: 'line',
          stack: 'pollutant',
          smooth: true,
          areaStyle: { opacity: 0.4 },
          data: this.trendData.map(d => d[key])
        }))
      }, true)
    },
    getAqiColor(aqi) {
      if (aqi <= 50) return '#00e400'
      if (aqi <= 100) return '#e6c700'
      if (aqi <= 150) return '#ff7e00'
      if (aqi <= 200) return '#ff0000'
      if (aqi <= 300) return '#99004c'
      return '#7e0023'
    },
    handleResize() {
      this.charts.forEach(c => c.resize())
    }
  }
}
</script>
