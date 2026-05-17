package com.navitaxi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DriverProfile Entity - Hồ sơ tài xế
 * Lưu năng lực tiếng Nhật (JLPT), số bằng lái, trạng thái sẵn sàng
 */
@Entity
@Table(name = "driver_profiles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DriverProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Integer driverId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /** Năng lực tiếng Nhật: N5, N4, N3, N2, N1 */
    @Column(name = "japanese_level", length = 20)
    private String japaneseLevel;

    @Column(name = "driving_experience_years")
    private Integer drivingExperienceYears;

    @Column(name = "license_number", length = 50)
    private String licenseNumber;

    @Column(name = "is_available")
    @Builder.Default
    private Boolean isAvailable = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
