package com.navitaxi.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO đăng ký tài khoản / 新規登録リクエスト
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Tên không được để trống / 名前は必須です")
    @Size(min = 2, max = 100, message = "Tên phải từ 2-100 ký tự / 名前は2〜100文字")
    private String fullName;

    @NotBlank(message = "Email không được để trống / メールアドレスは必須です")
    @Email(message = "Email không hợp lệ / メールアドレスの形式が正しくありません")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống / パスワードは必須です")
    @Size(min = 8, message = "Mật khẩu phải ít nhất 8 ký tự / パスワードは8文字以上")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Mật khẩu phải có chữ hoa, chữ thường, số và ký tự đặc biệt / パスワードには大文字、小文字、数字、特殊文字が必要です"
    )
    private String password;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống / パスワード確認は必須です")
    private String confirmPassword;

    private String phoneNumber;

    /** CUSTOMER hoặc DRIVER */
    @NotBlank(message = "Vai trò không được để trống / 役割は必須です")
    private String role;

    // --- Thông tin bổ sung cho tài xế ---
    /** Năng lực tiếng Nhật: N5, N4, N3, N2, N1 */
    private String japaneseLevel;

    /** Số bằng lái xe */
    private String licenseNumber;
}
