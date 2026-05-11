package com.navitaxi.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * RideRequest Entity - Yêu cầu đặt xe
 * Lưu điểm đi, điểm đến, loại xe, giá dự kiến, trạng thái
 */
@Entity
@Table(name = "ride_requests")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_id")
    private Integer rideId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "pickup_address", length = 255)
    private String pickupAddress;

    @Column(name = "pickup_lat", precision = 10, scale = 7)
    private BigDecimal pickupLat;

    @Column(name = "pickup_lng", precision = 10, scale = 7)
    private BigDecimal pickupLng;

    @Column(name = "destination_address", length = 255)
    private String destinationAddress;

    @Column(name = "destination_lat", precision = 10, scale = 7)
    private BigDecimal destinationLat;

    @Column(name = "destination_lng", precision = 10, scale = 7)
    private BigDecimal destinationLng;

    /** Loại xe yêu cầu: COMPACT, SEDAN, SUV, PREMIUM */
    @Column(name = "requested_vehicle_type", length = 20)
    private String requestedVehicleType;

    @Column(name = "estimated_distance_km", precision = 10, scale = 2)
    private BigDecimal estimatedDistanceKm;

    @Column(name = "estimated_duration_min", precision = 10, scale = 2)
    private BigDecimal estimatedDurationMin;

    @Column(name = "estimated_fare", precision = 15, scale = 2)
    private BigDecimal estimatedFare;

    @Column(name = "final_fare", precision = 15, scale = 2)
    private BigDecimal finalFare;

    /** Trạng thái: PENDING, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED */
    @Column(name = "status", length = 30)
    @Builder.Default
    private String status = "PENDING";

    @Column(name = "cancel_reason", length = 255)
    private String cancelReason;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "status_at")
    private LocalDateTime statusAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @PrePersist
    protected void onCreate() {
        this.requestedAt = LocalDateTime.now();
    }
}
