<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-input v-model="keyword" placeholder="搜索站点名称/编码/城市" clearable style="width:280px"
          @keyup.enter.native="loadData" />
        <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增站点</el-button>
      </div>

      <el-table :data="tableData" border stripe size="small">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="stationName" label="站点名称" width="180" />
        <el-table-column prop="stationCode" label="站点编码" width="100" />
        <el-table-column prop="cityName" label="所属城市" width="100" />
        <el-table-column prop="address" label="详细地址" min-width="180" />
        <el-table-column prop="longitude" label="经度" width="100" />
        <el-table-column prop="latitude" label="纬度" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="mini">
              {{ scope.row.status === 1 ? '运行中' : '已停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" style="color:#F56C6C" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="站点名称">
          <el-input v-model="form.stationName" />
        </el-form-item>
        <el-form-item label="站点编码">
          <el-input v-model="form.stationCode" />
        </el-form-item>
        <el-form-item label="所属城市">
          <el-select v-model="form.cityId" placeholder="选择城市" filterable style="width:100%">
            <el-option v-for="c in cityList" :key="c.id" :label="c.cityName" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="经度"><el-input-number v-model="form.longitude" :precision="4" style="width:100%" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度"><el-input-number v-model="form.latitude" :precision="4" style="width:100%" /></el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">运行中</el-radio>
            <el-radio :label="0">已停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getStationList, saveStation, updateStation, deleteStation, getCityList } from '@/api'

export default {
  name: 'StationList',
  data() {
    return {
      keyword: '',
      tableData: [],
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
      const res = await getStationList(this.keyword)
      this.tableData = res.data || []
    },
    async loadCities() {
      const res = await getCityList()
      this.cityList = res.data || []
    },
    handleAdd() {
      this.form = { status: 1 }
      this.dialogTitle = '新增监测站点'
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.form = { ...row }
      this.dialogTitle = '编辑监测站点'
      this.dialogVisible = true
    },
    async handleSubmit() {
      if (this.form.id) {
        await updateStation(this.form)
      } else {
        await saveStation(this.form)
      }
      this.$message.success('操作成功')
      this.dialogVisible = false
      this.loadData()
    },
    handleDelete(id) {
      this.$confirm('确认删除该站点？', '提示', { type: 'warning' }).then(async () => {
        await deleteStation(id)
        this.$message.success('删除成功')
        this.loadData()
      }).catch(() => {})
    }
  }
}
</script>
