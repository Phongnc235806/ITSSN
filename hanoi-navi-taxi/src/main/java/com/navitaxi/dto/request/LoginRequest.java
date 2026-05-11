package com.navitaxi.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO đăng nhập / ログインリクエスト
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Email không được để trống / メールアドレスは必須です")
    @Email(message = "Email không hợp lệ / メールアドレスの形式が正しくありません")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống / パスワードは必須です")
    private String password;

    private boolean rememberMe;
}
