package com.navitaxi.dto.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * Profile payload shared by customer profile, driver profile, and driver detail screens.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProfileResponse {

    private Integer userId;
    private Integer driverId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String avatarUrl;
    private String role;

    private String japaneseLevel;
    private Integer drivingExperienceYears;
    private Boolean isAvailable;
    private BigDecimal ratingAverage;
    private Integer reviewCount;

    private VehicleInfo vehicle;

    /**
     * Vehicle summary for driver profile UI. License plate is intentionally excluded.
     */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class VehicleInfo {
        private Integer vehicleId;
        private String vehicleType;
        private String brand;
        private String color;
        private String status;
    }
}
