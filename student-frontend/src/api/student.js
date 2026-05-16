import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

export default {
  list() {
    return api.get('/students')
  },
  get(id) {
    return api.get(`/students/${id}`)
  },
  create(data) {
    return api.post('/students', data)
  },
  update(id, data) {
    return api.put(`/students/${id}`, data)
  },
  remove(id) {
    return api.delete(`/students/${id}`)
  },
}
