package com.navitaxi.service;

import com.navitaxi.dto.request.LoginRequest;
import com.navitaxi.dto.request.RegisterRequest;
import com.navitaxi.dto.response.AuthResponse;
import com.navitaxi.exception.BadRequestException;
import com.navitaxi.model.DriverProfile;
import com.navitaxi.model.User;
import com.navitaxi.repository.DriverProfileRepository;
import com.navitaxi.repository.UserRepository;
import com.navitaxi.security.JwtTokenProvider;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication Service - Xử lý đăng ký và đăng nhập
 * 認証サービス - 登録とログインの処理
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       DriverProfileRepository driverProfileRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.driverProfileRepository = driverProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Đăng ký tài khoản mới / 新規アカウント登録
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 1. Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                "Email đã được sử dụng / このメールアドレスは既に使用されています");
        }

        // 2. Kiểm tra mật khẩu khớp
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException(
                "Mật khẩu xác nhận không khớp / パスワードが一致しません");
        }

        // 3. Kiểm tra role hợp lệ
        String role = request.getRole().toUpperCase();
        if (!role.equals("CUSTOMER") && !role.equals("DRIVER")) {
            throw new BadRequestException(
                "Vai trò không hợp lệ / 無効な役割です");
        }

        // 4. Tạo user mới
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .status("ACTIVE")
                .build();

        user = userRepository.save(user);

        // 5. Nếu là tài xế, tạo driver profile
        if ("DRIVER".equals(role)) {
            DriverProfile driverProfile = DriverProfile.builder()
                    .user(user)
                    .japaneseLevel(request.getJapaneseLevel())
                    .licenseNumber(request.getLicenseNumber())
                    .isAvailable(false)
                    .build();
            driverProfileRepository.save(driverProfile);
        }

        // 6. Tạo JWT token
        String token = jwtTokenProvider.generateToken(user.getEmail());

        return AuthResponse.of(token, role, user.getUserId(), user.getFullName(), user.getEmail());
    }

    /**
     * Đăng nhập / ログイン
     */
    public AuthResponse login(LoginRequest request) {
        // 1. Xác thực với Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));

        // 2. Lấy thông tin user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException(
                    "Không tìm thấy tài khoản / アカウントが見つかりません"));

        // 3. Kiểm tra trạng thái tài khoản
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BadRequestException(
                "Tài khoản đã bị khóa / アカウントがロックされています");
        }

        // 4. Tạo JWT token
        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponse.of(token, user.getRole(), user.getUserId(), user.getFullName(), user.getEmail());
    }

    /**
     * Lấy thông tin user từ email / メールアドレスからユーザー情報を取得
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException(
                    "Không tìm thấy tài khoản / アカウントが見つかりません"));
    }
}
