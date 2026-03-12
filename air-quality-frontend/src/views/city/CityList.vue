<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-input v-model="keyword" placeholder="搜索城市/省份" clearable style="width:250px"
          @keyup.enter.native="loadData" />
        <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增城市</el-button>
      </div>

      <el-table :data="tableData" border stripe size="small">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="cityName" label="城市名称" width="120" />
        <el-table-column prop="province" label="所属省份" width="120" />
        <el-table-column prop="longitude" label="经度" width="120" />
        <el-table-column prop="latitude" label="纬度" width="120" />
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="160">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" style="color:#F56C6C" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="450px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="城市名称">
          <el-input v-model="form.cityName" />
        </el-form-item>
        <el-form-item label="所属省份">
          <el-input v-model="form.province" />
        </el-form-item>
        <el-form-item label="经度">
          <el-input-number v-model="form.longitude" :precision="4" style="width:100%" />
        </el-form-item>
        <el-form-item label="纬度">
          <el-input-number v-model="form.latitude" :precision="4" style="width:100%" />
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
import { getCityList, saveCity, updateCity, deleteCity } from '@/api'

export default {
  name: 'CityList',
  data() {
    return {
      keyword: '',
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {}
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      const res = await getCityList(this.keyword)
      this.tableData = res.data || []
    },
    handleAdd() {
      this.form = {}
      this.dialogTitle = '新增城市'
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.form = { ...row }
      this.dialogTitle = '编辑城市'
      this.dialogVisible = true
    },
    async handleSubmit() {
      if (this.form.id) {
        await updateCity(this.form)
      } else {
        await saveCity(this.form)
      }
      this.$message.success('操作成功')
      this.dialogVisible = false
      this.loadData()
    },
    handleDelete(id) {
      this.$confirm('确认删除该城市？', '提示', { type: 'warning' }).then(async () => {
        await deleteCity(id)
        this.$message.success('删除成功')
        this.loadData()
      }).catch(() => {})
    }
  }
}
</script>
