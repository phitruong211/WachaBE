# Backlog - Wacha Cinema Backend

## Tech Stack

| Công nghệ | Version | Mục đích |
|-----------|---------|----------|
| Java | 21 | Ngôn ngữ chính |
| Spring Boot | 3.3.2 | Framework backend |
| Spring Data JPA | 3.3.2 | ORM, truy vấn database |
| Spring Security | 6.3.x | Xác thực & phân quyền |
| Spring Validation | 3.3.2 | Validate request DTO |
| MySQL | 8+ | Database chính (production) |
| H2 | 2.2.x | Database in-memory (test) |
| Lombok | 1.18.x | Giảm boilerplate code |
| MapStruct | 1.5.5 | Object mapping (processor) |
| JJWT | 0.12.3 | Tạo & xác thực JWT token |
| Springdoc OpenAPI | 2.3.0 | Swagger UI + API docs |
| Spring Dotenv | 4.0.0 | Load biến môi trường từ `.env` |
| Docker | Multi-stage | Containerize ứng dụng |

---

## Thư viện & Annotation chi tiết

### 1. Spring Boot Core
```
spring-boot-starter-web        → @RestController, @RequestMapping, @GetMapping, @PostMapping...
spring-boot-starter-data-jpa   → @Entity, @Repository, JpaRepository, @Query
spring-boot-starter-validation → @Valid, @NotNull, @NotBlank, @Size, @Min, @Max, @Email
spring-boot-starter-security   → @EnableWebSecurity, @EnableMethodSecurity, SecurityFilterChain
spring-boot-starter-test       → @SpringBootTest, @ActiveProfiles
```

### 2. JPA & Entity Annotations
```java
@Entity                    // Đánh dấu class là JPA entity
@Table(name, uniqueConstraints) // Tên bảng + ràng buộc unique
@Id                        // Primary key
@GeneratedValue(IDENTITY)  // Auto increment
@Column(nullable, unique)  // Cấu hình cột
@ManyToOne(fetch = LAZY)   // Quan hệ nhiều-một (User)
@JoinColumn(name)          // Tên cột foreign key
@Enumerated(EnumType.STRING) // Lưu enum dạng string
@PrePersist                // Callback trước khi INSERT
@PreUpdate                 // Callback trước khi UPDATE
```

### 3. Spring Security + JWT
```java
@EnableWebSecurity         // Bật cấu hình security
@EnableMethodSecurity      // Bật @PreAuthorize trên method
@Configuration             // Class cấu hình Spring
SecurityFilterChain        // Cấu hình HTTP security rules
UsernamePasswordAuthenticationToken // Token xác thực
@AuthenticationPrincipal   // Inject user đang đăng nhập vào controller
OncePerRequestFilter       // Base class cho JWT filter (chạy 1 lần/request)
BCryptPasswordEncoder      // Mã hoá mật khẩu
```

**JWT Flow:**
```
Register/Login → BCrypt verify → JwtService.generateToken() → trả AccessToken + RefreshToken
Request → JwtAuthenticationFilter → JwtService.extractUsername() → validateToken() → set SecurityContext
```

**Thư viện JWT (jjwt 0.12.3):**
```java
Jwts.builder()             // Tạo token
    .subject(username)     // Subject = username
    .issuedAt(now)         // Thời điểm phát hành
    .expiration(exp)       // Hết hạn
    .signWith(key)         // Ký bằng HMAC-SHA key
    .compact()             // Build thành string

Jwts.parser()              // Parse token
    .verifyWith(key)       // Verify chữ ký
    .parseSignedClaims()   // Đọc claims
```

### 4. Lombok Annotations
```java
@Getter @Setter            // Tự sinh getter/setter
@Builder                   // Builder pattern
@NoArgsConstructor         // Constructor không tham số
@AllArgsConstructor        // Constructor đầy đủ tham số
@RequiredArgsConstructor   // Constructor cho final fields (dùng cho DI)
@Data                      // @Getter + @Setter + @ToString + @EqualsAndHashCode
@Slf4j                     // Logger tự động (log.info, log.error...)
```

### 5. Validation Annotations
```java
@Valid                     // Kích hoạt validate trên @RequestBody
@NotNull(message)          // Không null
@NotBlank(message)         // Không null/empty/blank
@Size(min, max)            // Độ dài chuỗi
@Min(value) @Max(value)    // Giá trị số min/max
@Email                     // Format email
```

### 6. Swagger / OpenAPI
```java
@Tag(name, description)    // Nhóm endpoints trên Swagger UI
@Operation(summary)        // Mô tả từng endpoint
```

**Cấu hình OpenAPI (OpenApiConfig.java):**
```java
OpenAPI → Info (title, version) + SecurityScheme (Bearer JWT)
```

### 7. Spring RestClient (gọi TMDB API)
```java
RestClient.builder()
    .baseUrl("https://api.themoviedb.org/3")
    .build()

restClient.get()
    .uri(uri -> uri.path("/movie/popular")
        .queryParam("api_key", key)
        .queryParam("page", page)
        .build())
    .retrieve()
    .body(TmdbMovieListResponse.class)
```

### 8. Jackson JSON Mapping (cho TMDB response)
```java
@JsonIgnoreProperties(ignoreUnknown = true)  // Bỏ qua field không cần
@JsonProperty("poster_path")                  // Map tên JSON field khác tên Java field
```

### 9. Spring Dotenv
```
Thư viện: me.paulschwarz:spring-dotenv:4.0.0
Tự động load file .env ở root project vào Spring Environment
application.properties dùng ${DB_URL}, ${JWT_SECRET}... → resolve từ .env
```

---

## Kiến trúc Flow

```
Client Request
    ↓
Controller (@RestController)
    ↓ @AuthenticationPrincipal lấy userId từ JWT
Service Layer (@Service, @Transactional)
    ├── TmdbService → TmdbClient (RestClient) → TMDB API v3
    └── Repository (@Repository, JpaRepository) → MySQL
    ↓
Response DTO → Client
```

### Flow chi tiết: GET /api/movies/{tmdbId}
```
MovieController.getMovieDetail()
    ↓ userId từ @AuthenticationPrincipal (nullable)
MovieService.getMovieDetail(tmdbId, userId, language)
    ├── TmdbService.getMovieDetail() → TmdbClient → TMDB API
    │       → build posterUrl = imageBaseUrl + "w500" + poster_path
    │       → build backdropUrl = imageBaseUrl + "w1280" + backdrop_path
    ├── TmdbService.getTrailerUrls() → lọc YouTube Trailer
    ├── FavoriteRepository.existsByUserIdAndTmdbId()
    ├── RatingRepository.findByUserIdAndTmdbId()
    ├── WatchingRepository.findByUserIdAndTmdbId()
    └── WatchlistRepository.existsByUserIdAndTmdbId()
    ↓ merge tất cả
MovieDetailResponse { tmdb data + isFavorite + userRating + watchingProgress + isInWatchlist }
```

---

## Cấu hình biến môi trường

| Biến | Mô tả | Dùng ở |
|------|-------|--------|
| `DB_URL` | JDBC URL MySQL | `spring.datasource.url` |
| `DB_USERNAME` | Username DB | `spring.datasource.username` |
| `DB_PASSWORD` | Password DB | `spring.datasource.password` |
| `JWT_SECRET` | HMAC-SHA key (hex) | `JwtConfig.secret` → `JwtService` |
| `JWT_SALT` | Salt bổ sung | `JwtConfig.salt` → `JwtService` |
| `TMDB_API_KEY` | TMDB API v3 key | `TmdbConfig.apiKey` → `TmdbClient` |

---

## Docker

### Dockerfile (multi-stage)
```
Stage 1 (build): eclipse-temurin:21-jdk-alpine → mvnw package
Stage 2 (run):   eclipse-temurin:21-jre-alpine → java -jar app.jar
```
- Image nhẹ (~150MB), non-root user
- `.dockerignore` loại `.git`, `.env`, `target/`, IDE files

### Deploy
```bash
# Truyền biến trực tiếp
DB_URL=... DB_USERNAME=... DB_PASSWORD=... JWT_SECRET=... JWT_SALT=... TMDB_API_KEY=... docker compose up --build -d

# Hoặc set biến trên platform (Render, Railway, Fly.io...)
```

---

## Changelog

| Ngày | Thay đổi |
|------|----------|
| 2026-02-24 | Tích hợp TMDB API v3 (realtime, không lưu DB) |
| 2026-02-24 | Refactor entity dùng tmdbId thay vì Movie FK |
| 2026-02-24 | Xoá Movie entity, MovieRepository, tất cả mapper cũ |
| 2026-02-24 | Tách secrets ra .env, gitignore |
| 2026-02-24 | Thêm Dockerfile, docker-compose.yml |
| 2026-02-24 | Thêm Swagger/OpenAPI với JWT bearer auth |
| 2026-02-24 | Rewrite toàn bộ Service/Controller theo kiến trúc mới |

## TODO

- [ ] Điền TMDB API key thật vào biến môi trường
- [ ] Thêm Redis cache cho TMDB response
- [ ] Token blacklist cho logout (Redis)
- [ ] Pagination cho user interaction list
- [ ] Unit test cho service layer
- [ ] Integration test cho controller layer

