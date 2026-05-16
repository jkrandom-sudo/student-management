<template>
  <div class="student-management">
    <header class="header">
      <h1>学生管理系统</h1>
      <button class="btn btn-primary" @click="openAdd">+ 新增学生</button>
    </header>

    <div class="search-bar">
      <input v-model="keyword" placeholder="搜索姓名..." @input="filterStudents" />
    </div>

    <table class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>姓名</th>
          <th>年龄</th>
          <th>性别</th>
          <th>年级</th>
          <th>专业</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="s in filtered" :key="s.id">
          <td>{{ s.id }}</td>
          <td>{{ s.name }}</td>
          <td>{{ s.age }}</td>
          <td>{{ s.gender }}</td>
          <td>{{ s.grade }}</td>
          <td>{{ s.major }}</td>
          <td class="actions">
            <button class="btn btn-sm" @click="openEdit(s)">编辑</button>
            <button class="btn btn-sm btn-danger" @click="handleDelete(s.id)">删除</button>
          </td>
        </tr>
        <tr v-if="filtered.length === 0">
          <td colspan="7" class="empty">暂无数据</td>
        </tr>
      </tbody>
    </table>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <h2>{{ isEdit ? '编辑学生' : '新增学生' }}</h2>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label>姓名</label>
            <input v-model="form.name" required />
          </div>
          <div class="form-group">
            <label>年龄</label>
            <input v-model.number="form.age" type="number" min="1" max="150" required />
          </div>
          <div class="form-group">
            <label>性别</label>
            <select v-model="form.gender" required>
              <option value="">请选择</option>
              <option value="男">男</option>
              <option value="女">女</option>
            </select>
          </div>
          <div class="form-group">
            <label>年级</label>
            <input v-model="form.grade" required placeholder="如：大一、大二" />
          </div>
          <div class="form-group">
            <label>专业</label>
            <input v-model="form.major" required />
          </div>
          <div class="form-actions">
            <button type="button" class="btn" @click="closeModal">取消</button>
            <button type="submit" class="btn btn-primary">{{ isEdit ? '保存' : '创建' }}</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import studentApi from '../api/student'

export default {
  name: 'StudentManagement',
  data() {
    return {
      students: [],
      keyword: '',
      showModal: false,
      isEdit: false,
      editId: null,
      form: { name: '', age: '', gender: '', grade: '', major: '' },
    }
  },
  computed: {
    filtered() {
      if (!this.keyword) return this.students
      return this.students.filter(s => s.name.includes(this.keyword))
    },
  },
  created() {
    this.loadStudents()
  },
  methods: {
    async loadStudents() {
      try {
        const { data } = await studentApi.list()
        this.students = data
      } catch (e) {
        alert('加载学生列表失败: ' + e.message)
      }
    },
    filterStudents() {},
    openAdd() {
      this.isEdit = false
      this.editId = null
      this.form = { name: '', age: '', gender: '', grade: '', major: '' }
      this.showModal = true
    },
    openEdit(s) {
      this.isEdit = true
      this.editId = s.id
      this.form = { name: s.name, age: s.age, gender: s.gender, grade: s.grade, major: s.major }
      this.showModal = true
    },
    closeModal() {
      this.showModal = false
    },
    async handleSubmit() {
      try {
        if (this.isEdit) {
          await studentApi.update(this.editId, this.form)
        } else {
          await studentApi.create(this.form)
        }
        this.closeModal()
        await this.loadStudents()
      } catch (e) {
        alert('操作失败: ' + e.message)
      }
    },
    async handleDelete(id) {
      if (!confirm('确定删除该学生吗？')) return
      try {
        await studentApi.remove(id)
        await this.loadStudents()
      } catch (e) {
        alert('删除失败: ' + e.message)
      }
    },
  },
}
</script>

<style scoped>
.student-management {
  max-width: 1000px;
  margin: 0 auto;
  padding: 24px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
  color: #1a1a2e;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h1 {
  font-size: 24px;
  margin: 0;
  color: #16213e;
}

.search-bar {
  margin-bottom: 16px;
}

.search-bar input {
  width: 300px;
  padding: 8px 14px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s;
}

.search-bar input:focus {
  border-color: #4361ee;
}

.table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
}

.table th {
  background: #16213e;
  color: #fff;
  padding: 12px 16px;
  text-align: left;
  font-weight: 500;
  font-size: 14px;
}

.table td {
  padding: 10px 16px;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
}

.table tr:hover td {
  background: #f8f9ff;
}

.empty {
  text-align: center;
  color: #9ca3af;
  padding: 40px !important;
}

.actions {
  display: flex;
  gap: 8px;
}

.btn {
  padding: 6px 16px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: #fff;
  color: #374151;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.15s;
}

.btn:hover {
  background: #f3f4f6;
}

.btn-primary {
  background: #4361ee;
  color: #fff;
  border-color: #4361ee;
}

.btn-primary:hover {
  background: #3a56d4;
}

.btn-sm {
  padding: 4px 12px;
  font-size: 13px;
}

.btn-danger {
  color: #ef4444;
  border-color: #ef4444;
}

.btn-danger:hover {
  background: #ef4444;
  color: #fff;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.modal {
  background: #fff;
  border-radius: 12px;
  padding: 28px;
  width: 420px;
  max-width: 90vw;
  box-shadow: 0 20px 60px rgba(0,0,0,0.15);
}

.modal h2 {
  margin: 0 0 20px;
  font-size: 18px;
  color: #16213e;
}

.form-group {
  margin-bottom: 14px;
}

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 500;
  margin-bottom: 4px;
  color: #4b5563;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s;
  box-sizing: border-box;
}

.form-group input:focus,
.form-group select:focus {
  border-color: #4361ee;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>
