package com.navitaxi.dto.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * Result of voucher validation and discount calculation.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class VoucherCalculationResponse {

    private boolean valid;
    private String voucherCode;
    private String description;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal originalFare;
    private BigDecimal discountAmount;
    private BigDecimal finalFare;
    private String message;
}
