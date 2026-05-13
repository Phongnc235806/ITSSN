package com.navitaxi.dto.response;

import lombok.*;

/**
 * DTO phản hồi xác thực / 認証レスポンス
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String tokenType;
    private String role;
    private String redirectUrl;
    private Integer userId;
    private String fullName;
    private String email;

    public static AuthResponse of(String token, String role, Integer userId, String fullName, String email) {
        String redirectUrl;
        switch (role) {
            case "DRIVER":
                redirectUrl = "/driver/home";
                break;
            case "ADMIN":
                redirectUrl = "/admin/dashboard";
                break;
            default:
                redirectUrl = "/customer/home";
                break;
        }

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .role(role)
                .redirectUrl(redirectUrl)
                .userId(userId)
                .fullName(fullName)
                .email(email)
                .build();
    }
}
