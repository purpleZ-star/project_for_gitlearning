<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增用户</el-button>
      </div>

      <el-table :data="tableData" border stripe size="small">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="role" label="角色" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.role === 1 ? 'danger' : ''" size="mini">
              {{ scope.row.role === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="mini">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="140">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" style="color:#F56C6C" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="450px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item :label="form.id ? '新密码' : '密码'">
          <el-input v-model="form.password" type="password" show-password
            :placeholder="form.id ? '留空则不修改' : ''" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="角色">
          <el-radio-group v-model="form.role">
            <el-radio :label="0">普通用户</el-radio>
            <el-radio :label="1">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
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
import { getUserList, saveUser, updateUser, deleteUser } from '@/api'

export default {
  name: 'UserList',
  data() {
    return {
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
      const res = await getUserList()
      this.tableData = res.data || []
    },
    handleAdd() {
      this.form = { role: 0, status: 1 }
      this.dialogTitle = '新增用户'
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.form = { ...row, password: '' }
      this.dialogTitle = '编辑用户'
      this.dialogVisible = true
    },
    async handleSubmit() {
      if (this.form.id) {
        await updateUser(this.form)
      } else {
        await saveUser(this.form)
      }
      this.$message.success('操作成功')
      this.dialogVisible = false
      this.loadData()
    },
    handleDelete(id) {
      this.$confirm('确认删除该用户？', '提示', { type: 'warning' }).then(async () => {
        await deleteUser(id)
        this.$message.success('删除成功')
        this.loadData()
      }).catch(() => {})
    }
  }
}
</script>
