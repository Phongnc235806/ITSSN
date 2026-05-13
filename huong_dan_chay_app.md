# Hướng dẫn chạy Hanoi Navi-Taxi

---

## Cách 1: Docker Compose 


### Bước 1 — Cài Docker Desktop
- Tải tại: https://www.docker.com/products/docker-desktop/
- Cài xong → khởi động Docker Desktop → đợi icon 🐳 xanh ở thanh taskbar

### Bước 2 — Mở Terminal trong thư mục project
```powershell
cd C:\Users\NITRO5\.gemini\antigravity\scratch\hanoi-navi-taxi
```

### Bước 3 — Chạy
```powershell
docker-compose up --build
```
- Lần đầu mất ~3-5 phút (tải image MySQL + build Java)
- Khi thấy dòng `Started NaviTaxiApplication` → thành công ✅

### Bước 4 — Mở trình duyệt
```
http://localhost:8080
```

### Dừng app
```powershell
docker-compose down
```

---

## Cách 2: Chạy thủ công (Maven + MySQL)

### Yêu cầu cài trước
| Phần mềm | Tải tại | Kiểm tra |
|-----------|---------|----------|
| **Java 17+** | https://adoptium.net/ | `java -version` |
| **Maven 3.8+** | https://maven.apache.org/download.cgi | `mvn -version` |
| **MySQL 8.0** | https://dev.mysql.com/downloads/installer/ | `mysql --version` |

### Bước 1 — Tạo database MySQL
Mở MySQL terminal hoặc MySQL Workbench, chạy:
```sql
CREATE DATABASE navitaxi_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Bước 2 — Kiểm tra cấu hình MySQL
Mở [application.yml](file:///C:/Users/NITRO5/.gemini/antigravity/scratch/hanoi-navi-taxi/src/main/resources/application.yml), kiểm tra dòng 15-17:
```yaml
    url: jdbc:mysql://localhost:3306/navitaxi_db...
    username: root       # ← đổi nếu khác
    password: root       # ← đổi nếu khác
```

### Bước 3 — Mở Terminal, chạy
```powershell
cd C:\Users\NITRO5\.gemini\antigravity\scratch\hanoi-navi-taxi
mvn spring-boot:run
```
- Lần đầu mất ~2-3 phút (tải dependencies)
- Khi thấy `Started NaviTaxiApplication` → thành công ✅

### Bước 4 — Mở trình duyệt
```
http://localhost:8080
```

---

## Sau khi mở app

| Trang | URL |
|-------|-----|
| Đăng nhập | http://localhost:8080/login |
| Đăng ký | http://localhost:8080/register |
| Swagger API | http://localhost:8080/swagger-ui.html |

**Tài khoản Admin mặc định:**
```
Email:    admin@navitaxi.com
Password: Admin@123
```

> [!IMPORTANT]
> Nếu gặp lỗi kết nối MySQL → kiểm tra MySQL đang chạy và username/password đúng trong `application.yml` dòng 16-17.
