package com.navitaxi.dto.response;

import lombok.*;
import java.util.Map;

/**
 * DTO phản hồi chung cho API / 汎用APIレスポンス
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ApiResponse {

    private boolean success;
    private String message;
    private Object data;
    private Map<String, String> errors;

    public static ApiResponse success(String message) {
        return ApiResponse.builder().success(true).message(message).build();
    }

    public static ApiResponse success(String message, Object data) {
        return ApiResponse.builder().success(true).message(message).data(data).build();
    }

    public static ApiResponse error(String message) {
        return ApiResponse.builder().success(false).message(message).build();
    }

    public static ApiResponse error(String message, Map<String, String> errors) {
        return ApiResponse.builder().success(false).message(message).errors(errors).build();
    }
}
