package com.navitaxi.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO yêu cầu đặt xe / 配車リクエスト
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BookingRequest {

    @NotBlank
    private String pickupAddress;
    private Double pickupLat;
    private Double pickupLng;

    @NotBlank
    private String destinationAddress;
    private Double destinationLat;
    private Double destinationLng;

    @NotBlank
    private String vehicleType;  // COMPACT, SEDAN, SUV, PREMIUM

    private Double estimatedDistanceKm;
    private Double estimatedDurationMin;
    private Double estimatedFare;

    private String voucherCode;
}
