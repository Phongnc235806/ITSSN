package com.navitaxi.dto.response;

import lombok.*;
import java.util.List;
import java.util.Map;

/**
 * DTO phản hồi tính lộ trình / ルート計算レスポンス
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RouteResponse {

    private String pickupAddress;
    private double pickupLat;
    private double pickupLng;

    private String destinationAddress;
    private double destinationLat;
    private double destinationLng;

    private double distanceKm;
    private String distanceText;
    private double durationMin;
    private String durationText;

    /** Encoded polyline for Google Maps rendering */
    private String encodedPolyline;

    /** Fare estimates per vehicle type */
    private List<FareEstimate> fareEstimates;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class FareEstimate {
        private String vehicleType;
        private String vehicleTypeJa;    // 日本語名
        private String vehicleTypeVi;    // Tên tiếng Việt
        private int seats;
        private double estimatedFare;
        private String fareFormatted;    // "150,000₫"
    }
}
