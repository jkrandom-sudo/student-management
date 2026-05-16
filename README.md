# 学生管理系统 (Student Management System) 

基于 Spring Boot + Vue 3 的全栈学生信息管理系统，支持学生信息的增删改查，通过 Nginx 反向代理统一入口，使用 Cpolar 内网穿透实现公网访问。

## 项目结构 

```
student-management/
├── student-backend/          # Spring Boot 后端
│   ├── pom.xml               # Maven 依赖配置
│   └── src/main/
│       ├── java/com/student/
│       │   ├── StudentApplication.java    # 启动入口
│       │   ├── config/CorsConfig.java     # CORS 跨域配置
│       │   ├── controller/StudentController.java  # REST API
│       │   ├── entity/Student.java        # 学生实体
│       │   └── repository/StudentRepository.java  # JPA 数据层
│       └── resources/application.yml      # 应用配置
├── student-frontend/         # Vue 3 前端
│   ├── vite.config.js        # Vite 配置（端口 3000）
│   ├── package.json
│   └── src/
│       ├── api/student.js    # Axios API 客户端
│       ├── components/StudentManagement.vue  # 主页面组件
│       ├── App.vue
│       ├── main.js
│       └── style.css
├── CLAUDE.md                 # Claude 项目指南
└── .gitignore
```

## 技术栈 

| 层级 | 技术 | 端口 |
|------|------|------|
| 前端 | Vue 3 + Vite 8 + Axios | 3000 |
| 反向代理 | Nginx | 8080 / 8181 |
| 后端 | Spring Boot 2.7.18 + JPA | 19090 |
| 数据库 | H2 File (./data/student) | - |
| 内网穿透 | Cpolar | 公网隧道 |

## 快速开始 

### 环境要求 

- JDK 1.8+
- Node.js 18+
- Maven 3.6+

### 启动后端 

```bash
cd student-backend
mvn clean package -DskipTests
java -jar target/student-backend-1.0.0.jar
```

### 启动前端 

```bash
cd student-frontend
npm install --registry=https://registry.npmmirror.com
npm run dev
```

### 启动 Nginx（可选，本地统一访问） 

```bash
/usr/local/opt/nginx/bin/nginx -t && /usr/local/opt/nginx/bin/nginx
```

### 启动 Cpolar 内网穿透（可选，公网访问） 

```bash
cpolar start-all
# 管理界面: http://127.0.0.1:4040
```

## 系统架构 

```
公网用户
    │
    ▼
Cpolar 隧道 (随机子域名)
    │
    ▼
Nginx (:8080 / :8181)  ──  同一域名，统一入口
    │
    ├── /api/*  ──►  Spring Boot (:19090)  ──►  H2 文件数据库
    │                   └── GET/POST/PUT/DELETE /api/students
    │
    └── /*      ──►  Vite Dev Server (:3000)
                        └── StudentManagement.vue (CRUD 界面)
```

## API 接口 

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/students | 获取全部学生 |
| GET | /api/students/{id} | 根据 ID 获取学生 |
| POST | /api/students | 新增学生 |
| PUT | /api/students/{id} | 修改学生信息 |
| DELETE | /api/students/{id} | 删除学生 |

学生字段：`id`, `name`, `age`, `gender`, `grade`, `major`

## 功能 

- 学生信息列表展示
- 按姓名搜索筛选
- 新增学生（弹窗表单）
- 编辑学生信息
- 删除学生（确认提示）

## 许可证 

MIT
