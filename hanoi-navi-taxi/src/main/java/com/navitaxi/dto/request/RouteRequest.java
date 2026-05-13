package com.navitaxi.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO yêu cầu tính lộ trình / ルート計算リクエスト
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RouteRequest {

    @NotBlank(message = "Vui lòng nhập điểm đón / 乗車地を入力してください")
    private String pickupAddress;

    private Double pickupLat;
    private Double pickupLng;

    @NotBlank(message = "Vui lòng nhập điểm đến / 降車地を入力してください")
    private String destinationAddress;

    private Double destinationLat;
    private Double destinationLng;
}
