# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

### Backend (Spring Boot)
```bash
cd student-backend
mvn clean package -DskipTests       # Build JAR
java -jar target/student-backend-1.0.0.jar   # Run on port 19090
mvn spring-boot:run                  # Run via Maven
```

### Frontend (Vue 3 + Vite)
```bash
cd student-frontend
npm install --registry=https://registry.npmmirror.com   # Install deps (use mirror)
npm run dev       # Dev server on port 3000
npm run build     # Production build to dist/
```

### Nginx
```bash
/usr/local/opt/nginx/bin/nginx -t                    # Test config
/usr/local/opt/nginx/bin/nginx -s reload             # Reload config
```

### Cpolar (内网穿透)
```bash
/Users/wangshuai/claude_project/tools/cpolar start-all   # Start all tunnels
# Config: ~/.cpolar/cpolar.yml
# Local web UI: http://127.0.0.1:4040
```

## Architecture

```
Cpolar Tunnel (public URL)
       │
       ▼
  Nginx (:8080 / :8181)
       │
       ├── /api/*  →  Spring Boot (:19090)  →  H2 file DB (./data/student)
       └── /*      →  Vite dev server (:3000)
```

- **Backend**: Spring Boot 2.7.18, Java 1.8, JPA + H2 file database. REST API at `/api/students` with CRUD operations. No service layer — controller calls repository directly.
- **Frontend**: Vue 3 (Options API) + Vite 8 + Axios. Single-page app with `StudentManagement.vue` component. API calls go to `/api` (resolved by nginx proxy, not Vite devServer proxy).
- **Nginx**: Reverse proxy on ports 8080 and 8181. Routes `/api/` to backend, everything else to frontend. `Host` header set to `localhost` on frontend proxy to bypass Vite host check.
- **Cpolar**: Two tunnels (`student` → 8080, `port8181` → 8181). Free tier assigns random subdomains that change on restart. Config at `~/.cpolar/cpolar.yml`.

## Key Files

- Backend entry: `student-backend/src/main/java/com/student/StudentApplication.java`
- Entity: `student-backend/src/main/java/com/student/entity/Student.java` — fields: id, name, age, gender, grade, major
- Controller: `student-backend/src/main/java/com/student/controller/StudentController.java` — `/api/students` CRUD
- Frontend API client: `student-frontend/src/api/student.js` — Axios with baseURL `/api`
- Frontend main component: `student-frontend/src/components/StudentManagement.vue`
- Nginx site config: `/usr/local/etc/nginx/servers/student.conf`
- Nginx main config: `/usr/local/etc/nginx/nginx.conf` — minimal, includes `servers/*`

## Notes

- Backend CORS config (`CorsConfig.java`) allows all origins — only needed for direct backend access; nginx proxy makes it redundant.
- H2 database is file-based (`jdbc:h2:file:./data/student`), data persists across restarts in `student-backend/data/`.
- `npm install` without mirror may hang in China — always use `--registry=https://registry.npmmirror.com`.
- Cpolar authtoken is stored in `~/.cpolar/cpolar.yml` — do not commit.
