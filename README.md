# Wacha Cinema - Backend API

Backend REST API cho ứng dụng xem phim Wacha Cinema.  
Dữ liệu phim được lấy **realtime từ TMDB API v3**, database chỉ lưu tương tác của người dùng.

---

## Tổng quan nghiệp vụ

### 1. Xác thực & Phân quyền
| Chức năng | Endpoint | Mô tả |
|-----------|----------|-------|
| Đăng ký | `POST /api/auth/register` | Tạo tài khoản mới, trả access token + refresh token |
| Đăng nhập | `POST /api/auth/login` | Xác thực bằng username/password, trả JWT |
| Refresh token | `POST /api/auth/refresh` | Cấp access token mới từ refresh token |
| Đổi mật khẩu | `PUT /api/auth/change-password` | Yêu cầu đăng nhập, xác nhận mật khẩu cũ |
| Đăng xuất | `POST /api/auth/logout` | Huỷ phiên đăng nhập |

- Mật khẩu được mã hoá bằng **BCrypt** trước khi lưu.
- JWT gồm access token (24h) và refresh token (7 ngày).
- Phân quyền: `USER` (mặc định) và `ADMIN`.

### 2. Duyệt phim (TMDB - Public)
| Chức năng | Endpoint | Mô tả |
|-----------|----------|-------|
| Phim phổ biến | `GET /api/movies/popular?page=&language=` | Danh sách phim hot từ TMDB |
| Phim đánh giá cao | `GET /api/movies/top-rated?page=&language=` | Top rated từ TMDB |
| Phim sắp chiếu | `GET /api/movies/upcoming?page=&language=` | Upcoming từ TMDB |
| Tìm kiếm | `GET /api/movies/search?query=&page=&language=` | Tìm phim theo từ khoá |
| Chi tiết phim | `GET /api/movies/{tmdbId}?language=` | Chi tiết + poster/backdrop đầy đủ URL. Nếu đã đăng nhập: kèm isFavorite, userRating, watchingProgress |
| Trailer | `GET /api/movies/{tmdbId}/trailers?language=` | Danh sách link YouTube trailer |
| Hình ảnh | `GET /api/movies/{tmdbId}/images` | Poster + backdrop với full URL |

- Poster URL = `https://image.tmdb.org/t/p/w500` + file_path
- Backdrop URL = `https://image.tmdb.org/t/p/w1280` + file_path
- **Không lưu metadata phim vào database**.

### 3. Yêu thích (Authenticated)
| Chức năng | Endpoint | Mô tả |
|-----------|----------|-------|
| Thêm yêu thích | `POST /api/favorites` | Body: `{ "tmdbId": 123 }` |
| Bỏ yêu thích | `DELETE /api/favorites/{tmdbId}` | Xoá khỏi danh sách |
| Danh sách yêu thích | `GET /api/favorites` | Của user hiện tại |
| Kiểm tra | `GET /api/favorites/check/{tmdbId}` | Trả `true/false` |

### 4. Đánh giá phim (Authenticated)
| Chức năng | Endpoint | Mô tả |
|-----------|----------|-------|
| Tạo đánh giá | `POST /api/ratings` | Body: `{ "tmdbId", "ratingValue" (1-10), "reviewText" }` |
| Sửa đánh giá | `PUT /api/ratings` | Cập nhật điểm + review |
| Xoá đánh giá | `DELETE /api/ratings/{tmdbId}` | Xoá đánh giá của mình |
| Đánh giá của tôi | `GET /api/ratings` | Danh sách rating của user |
| Đánh giá theo phim | `GET /api/ratings/movie/{tmdbId}` | Public - xem tất cả rating |
| Điểm trung bình | `GET /api/ratings/movie/{tmdbId}/average` | Public |

### 5. Tiến độ xem phim (Authenticated)
| Chức năng | Endpoint | Mô tả |
|-----------|----------|-------|
| Bắt đầu/Cập nhật | `POST /api/watching` | Body: `{ "tmdbId", "progressTime", "progressPercentage", "status" }` |
| Xem tiến độ | `GET /api/watching/{tmdbId}` | Xem đang coi tới đâu |
| Lịch sử xem | `GET /api/watching` | Tất cả phim đã/đang xem |
| Theo trạng thái | `GET /api/watching/status/{status}` | WATCHING / PAUSED / COMPLETED |
| Xoá | `DELETE /api/watching/{tmdbId}` | Xoá record |

### 6. Danh sách xem sau (Authenticated)
| Chức năng | Endpoint | Mô tả |
|-----------|----------|-------|
| Thêm | `POST /api/watchlist` | Body: `{ "tmdbId", "status" }` |
| Cập nhật trạng thái | `PUT /api/watchlist/{tmdbId}?status=` | PLANNED / WATCHED / SKIPPED |
| Xoá | `DELETE /api/watchlist/{tmdbId}` | Bỏ khỏi watchlist |
| Danh sách | `GET /api/watchlist` | Watchlist của user |
| Theo trạng thái | `GET /api/watchlist/status/{status}` | Lọc theo status |
| Kiểm tra | `GET /api/watchlist/check/{tmdbId}` | Trả `true/false` |

### 7. Quản lý User (Authenticated)
| Chức năng | Endpoint | Mô tả |
|-----------|----------|-------|
| Xem profile | `GET /api/users/me` | Thông tin user hiện tại |
| Cập nhật profile | `PUT /api/users/me` | Sửa username, email, avatar |
| Xem user khác | `GET /api/users/{id}` | Theo id |
| Danh sách user | `GET /api/users` | Admin only |
| Xoá user | `DELETE /api/users/{id}` | Admin only |

---

## Cấu trúc thư mục

```
src/main/java/com/example/movieapp/
├── config/          # SecurityConfig, JwtConfig, OpenApiConfig
├── controller/      # REST controllers
├── dto/
│   ├── request/     # Request DTOs
│   └── response/    # Response DTOs
├── exception/       # GlobalExceptionHandler, ResourceNotFoundException
├── integration/
│   └── tmdb/        # TmdbClient, TmdbService, TmdbConfig, dto/
├── model/           # JPA Entities (User, Favorite, Rating, Watching, Watchlist)
├── repository/      # Spring Data JPA Repositories
├── security/        # JWT filter, UserDetails, JwtService
└── service/         # Business logic
```

---

## Cài đặt & Chạy

### Yêu cầu
- Java 21
- MySQL 8+
- TMDB API Key ([đăng ký tại đây](https://www.themoviedb.org/settings/api))

### Cấu hình
Tạo file `.env` ở root project (xem `.env.example`):
```env
DB_URL=jdbc:mysql://localhost:3306/movieapp
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=your_hex_key
JWT_SALT=your_hex_salt
TMDB_API_KEY=your_tmdb_key
PORT=8080
```

### Chạy local
```bash
./mvnw spring-boot:run
```

### Chạy bằng Docker
```bash
docker compose up --build -d
```

### Swagger UI
```
http://localhost:8080/swagger-ui.html
```

---

## Database Schema

Database **chỉ lưu user interaction**, không lưu metadata phim:

| Table | Columns |
|-------|---------|
| `users` | id, username, email, password, role, status, avatar_url, last_login_at |
| `favorites` | id, user_id, tmdb_id, created_at |
| `ratings` | id, user_id, tmdb_id, rating_value, review_text, rated_at |
| `watching` | id, user_id, tmdb_id, progress_time, progress_percentage, status, started_at, last_watched_at, completed_at |
| `watchlists` | id, user_id, tmdb_id, status, added_at |

