package com.navitaxi.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Voucher option displayed on the driver detail / voucher selection screen.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class VoucherOptionResponse {

    private Integer voucherId;
    private String code;
    private String description;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal originalFare;
    private BigDecimal discountAmount;
    private BigDecimal finalFare;
}
