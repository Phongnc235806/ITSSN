package com.navitaxi.controller;

import com.navitaxi.dto.request.LoginRequest;
import com.navitaxi.dto.request.RegisterRequest;
import com.navitaxi.dto.response.ApiResponse;
import com.navitaxi.dto.response.AuthResponse;
import com.navitaxi.model.User;
import com.navitaxi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Auth Controller - API xác thực (Đăng ký / Đăng nhập)
 * 認証コントローラー - 登録・ログインAPI
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "認証API / API Xác thực")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/register - Đăng ký tài khoản mới
     * 新規アカウント登録
     */
    @PostMapping("/register")
    @Operation(summary = "Đăng ký / 新規登録", description = "Tạo tài khoản mới với email, mật khẩu và vai trò")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(
                "Đăng ký thành công / 登録が完了しました", authResponse));
    }

    /**
     * POST /api/auth/login - Đăng nhập
     * ログイン
     */
    @PostMapping("/login")
    @Operation(summary = "Đăng nhập / ログイン", description = "Đăng nhập bằng email và mật khẩu")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(
                "Đăng nhập thành công / ログインしました", authResponse));
    }

    /**
     * GET /api/auth/me - Lấy thông tin user hiện tại
     * 現在のユーザー情報を取得
     */
    @GetMapping("/me")
    @Operation(summary = "Thông tin cá nhân / ユーザー情報", description = "Lấy thông tin người dùng đang đăng nhập")
    public ResponseEntity<ApiResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Chưa đăng nhập / ログインしていません"));
        }
        User user = authService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("OK", new UserInfo(user)));
    }

    /**
     * Inner DTO để không expose password hash
     */
    private record UserInfo(Integer userId, String email, String fullName,
                            String phoneNumber, String role, String status) {
        UserInfo(User user) {
            this(user.getUserId(), user.getEmail(), user.getFullName(),
                 user.getPhoneNumber(), user.getRole(), user.getStatus());
        }
    }
}
