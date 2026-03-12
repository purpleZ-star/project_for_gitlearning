<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-input v-model="query.cityName" placeholder="城市名称" clearable style="width:150px" />
        <el-select v-model="query.qualityLevel" placeholder="质量等级" clearable style="width:130px">
          <el-option label="优" value="优" />
          <el-option label="良" value="良" />
          <el-option label="轻度污染" value="轻度污染" />
          <el-option label="中度污染" value="中度污染" />
          <el-option label="重度污染" value="重度污染" />
          <el-option label="严重污染" value="严重污染" />
        </el-select>
        <el-date-picker v-model="dateRange" type="daterange" range-separator="至"
          start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd"
          style="width:280px" @change="handleDateChange" />
        <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </div>

      <el-table :data="tableData" border stripe size="small" v-loading="loading">
        <el-table-column prop="cityName" label="城市" width="100" />
        <el-table-column prop="aqi" label="AQI" width="70">
          <template slot-scope="scope">
            <span :style="{ color: getAqiColor(scope.row.aqi), fontWeight: 'bold' }">
              {{ scope.row.aqi }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="qualityLevel" label="等级" width="90">
          <template slot-scope="scope">
            <el-tag :type="getLevelType(scope.row.qualityLevel)" size="mini">
              {{ scope.row.qualityLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="primaryPollutant" label="首要污染物" width="100" />
        <el-table-column prop="pm25" label="PM2.5" width="70" />
        <el-table-column prop="pm10" label="PM10" width="70" />
        <el-table-column prop="so2" label="SO2" width="60" />
        <el-table-column prop="no2" label="NO2" width="60" />
        <el-table-column prop="co" label="CO" width="60" />
        <el-table-column prop="o3" label="O3" width="60" />
        <el-table-column prop="temperature" label="温度" width="60" />
        <el-table-column prop="humidity" label="湿度" width="60" />
        <el-table-column prop="monitorTime" label="监测时间" width="155" />
        <el-table-column label="操作" width="140" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" style="color:#F56C6C" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination style="margin-top:15px; text-align:right" @current-change="handlePageChange"
        @size-change="handleSizeChange" :current-page="query.pageNum" :page-size="query.pageSize"
        :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="100px" size="small">
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="城市">
              <el-select v-model="form.cityId" placeholder="选择城市" filterable>
                <el-option v-for="c in cityList" :key="c.id" :label="c.cityName" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="AQI">
              <el-input-number v-model="form.aqi" :min="0" :max="500" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="质量等级">
              <el-select v-model="form.qualityLevel">
                <el-option label="优" value="优" />
                <el-option label="良" value="良" />
                <el-option label="轻度污染" value="轻度污染" />
                <el-option label="中度污染" value="中度污染" />
                <el-option label="重度污染" value="重度污染" />
                <el-option label="严重污染" value="严重污染" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="首要污染物">
              <el-input v-model="form.primaryPollutant" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8"><el-form-item label="PM2.5"><el-input-number v-model="form.pm25" :precision="1" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="PM10"><el-input-number v-model="form.pm10" :precision="1" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="SO2"><el-input-number v-model="form.so2" :precision="1" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8"><el-form-item label="NO2"><el-input-number v-model="form.no2" :precision="1" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="CO"><el-input-number v-model="form.co" :precision="2" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="O3"><el-input-number v-model="form.o3" :precision="1" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="8"><el-form-item label="温度"><el-input-number v-model="form.temperature" :precision="1" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="湿度"><el-input-number v-model="form.humidity" :precision="1" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="风速"><el-input-number v-model="form.windSpeed" :precision="1" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="风向">
              <el-select v-model="form.windDirection">
                <el-option v-for="d in ['北','东北','东','东南','南','西南','西','西北']" :key="d" :label="d" :value="d" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监测时间">
              <el-date-picker v-model="form.monitorTime" type="datetime" placeholder="选择时间" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getAirQualityList, saveAirQuality, updateAirQuality, deleteAirQuality, getCityList } from '@/api'

export default {
  name: 'AirQualityList',
  data() {
    return {
      query: { cityName: '', qualityLevel: '', startTime: '', endTime: '', pageNum: 1, pageSize: 10 },
      dateRange: [],
      tableData: [],
      total: 0,
      loading: false,
      dialogVisible: false,
      dialogTitle: '',
      form: {},
      cityList: []
    }
  },
  mounted() {
    this.loadData()
    this.loadCities()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await getAirQualityList(this.query)
        this.tableData = res.data.list || []
        this.total = res.data.total || 0
      } finally {
        this.loading = false
      }
    },
    async loadCities() {
      const res = await getCityList()
      this.cityList = res.data || []
    },
    handleDateChange(val) {
      if (val) {
        this.query.startTime = val[0]
        this.query.endTime = val[1]
      } else {
        this.query.startTime = ''
        this.query.endTime = ''
      }
    },
    handlePageChange(page) {
      this.query.pageNum = page
      this.loadData()
    },
    handleSizeChange(size) {
      this.query.pageSize = size
      this.loadData()
    },
    handleAdd() {
      this.form = {}
      this.dialogTitle = '新增空气质量数据'
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.form = { ...row }
      this.dialogTitle = '编辑空气质量数据'
      this.dialogVisible = true
    },
    async handleSubmit() {
      try {
        if (this.form.id) {
          await updateAirQuality(this.form)
        } else {
          await saveAirQuality(this.form)
        }
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.loadData()
      } catch (e) { /* handled by interceptor */ }
    },
    handleDelete(id) {
      this.$confirm('确认删除该条数据？', '提示', { type: 'warning' }).then(async () => {
        await deleteAirQuality(id)
        this.$message.success('删除成功')
        this.loadData()
      }).catch(() => {})
    },
    getAqiColor(aqi) {
      if (aqi <= 50) return '#00e400'
      if (aqi <= 100) return '#e6c700'
      if (aqi <= 150) return '#ff7e00'
      if (aqi <= 200) return '#ff0000'
      if (aqi <= 300) return '#99004c'
      return '#7e0023'
    },
    getLevelType(level) {
      const map = { '优': 'success', '良': '', '轻度污染': 'warning', '中度污染': 'danger', '重度污染': 'danger', '严重污染': 'danger' }
      return map[level] || 'info'
    }
  }
}
</script>
