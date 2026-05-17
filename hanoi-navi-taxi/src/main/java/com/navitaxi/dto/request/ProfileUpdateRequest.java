package com.navitaxi.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Request body for updating the logged-in user's profile.
 * Driver-only fields are ignored for customer accounts by the service layer.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProfileUpdateRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    private String fullName;

    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 255)
    private String address;

    @Size(max = 512)
    private String avatarUrl;

    @Pattern(regexp = "^(N5|N4|N3|N2|N1)?$")
    private String japaneseLevel;

    @Min(0)
    @Max(80)
    private Integer drivingExperienceYears;

    private Boolean isAvailable;

    /** Current vehicle information shown on the driver profile screen. */
    @Pattern(regexp = "^(COMPACT|SEDAN|SUV|PREMIUM)?$")
    private String vehicleType;

    @Size(max = 100)
    private String vehicleBrand;

    @Size(max = 50)
    private String vehicleColor;
}
