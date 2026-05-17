package com.navitaxi.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Request body for validating a user-assigned voucher and calculating the fare after discount.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class VoucherCalculationRequest {

    @NotBlank
    @Size(max = 50)
    private String voucherCode;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal originalFare;
}
