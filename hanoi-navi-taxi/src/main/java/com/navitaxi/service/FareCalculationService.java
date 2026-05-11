package com.navitaxi.service;

import com.navitaxi.dto.response.RouteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;

/**
 * Fare Calculation Service - Tính giá cước theo loại xe
 * 料金計算サービス - 車種別料金計算
 */
@Service
public class FareCalculationService {

    @Value("${fare.compact.base:15000}")
    private double compactBase;
    @Value("${fare.compact.per-km-short:12000}")
    private double compactPerKmShort;
    @Value("${fare.compact.per-km-long:10000}")
    private double compactPerKmLong;

    @Value("${fare.sedan.base:20000}")
    private double sedanBase;
    @Value("${fare.sedan.per-km-short:15000}")
    private double sedanPerKmShort;
    @Value("${fare.sedan.per-km-long:12000}")
    private double sedanPerKmLong;

    @Value("${fare.suv.base:25000}")
    private double suvBase;
    @Value("${fare.suv.per-km-short:18000}")
    private double suvPerKmShort;
    @Value("${fare.suv.per-km-long:15000}")
    private double suvPerKmLong;

    @Value("${fare.premium.base:35000}")
    private double premiumBase;
    @Value("${fare.premium.per-km-short:25000}")
    private double premiumPerKmShort;
    @Value("${fare.premium.per-km-long:20000}")
    private double premiumPerKmLong;

    @Value("${fare.long-distance-threshold:30}")
    private double longDistanceThreshold;

    /**
     * Tính giá cước cho tất cả loại xe
     * 全車種の料金を計算
     */
    public List<RouteResponse.FareEstimate> calculateAllFares(double distanceKm) {
        List<RouteResponse.FareEstimate> estimates = new ArrayList<>();

        estimates.add(buildEstimate("COMPACT", "コンパクト", "Xe nhỏ", 4,
                calculateFare(distanceKm, compactBase, compactPerKmShort, compactPerKmLong)));

        estimates.add(buildEstimate("SEDAN", "セダン", "Sedan", 4,
                calculateFare(distanceKm, sedanBase, sedanPerKmShort, sedanPerKmLong)));

        estimates.add(buildEstimate("SUV", "SUV", "SUV 7 chỗ", 7,
                calculateFare(distanceKm, suvBase, suvPerKmShort, suvPerKmLong)));

        estimates.add(buildEstimate("PREMIUM", "プレミアム", "Premium", 4,
                calculateFare(distanceKm, premiumBase, premiumPerKmShort, premiumPerKmLong)));

        return estimates;
    }

    /**
     * Tính giá cước cho 1 loại xe cụ thể
     */
    public double calculateFareForType(double distanceKm, String vehicleType) {
        return switch (vehicleType.toUpperCase()) {
            case "COMPACT" -> calculateFare(distanceKm, compactBase, compactPerKmShort, compactPerKmLong);
            case "SEDAN" -> calculateFare(distanceKm, sedanBase, sedanPerKmShort, sedanPerKmLong);
            case "SUV" -> calculateFare(distanceKm, suvBase, suvPerKmShort, suvPerKmLong);
            case "PREMIUM" -> calculateFare(distanceKm, premiumBase, premiumPerKmShort, premiumPerKmLong);
            default -> calculateFare(distanceKm, sedanBase, sedanPerKmShort, sedanPerKmLong);
        };
    }

    /**
     * Công thức tính giá:
     * - Giá = Giá mở cửa + (km * giá/km)
     * - Nếu < 30km: dùng giá/km ngắn
     * - Nếu >= 30km: 30km đầu dùng giá ngắn, phần còn lại dùng giá dài
     */
    private double calculateFare(double distanceKm, double base, double perKmShort, double perKmLong) {
        if (distanceKm <= longDistanceThreshold) {
            return base + (distanceKm * perKmShort);
        } else {
            double shortPart = longDistanceThreshold * perKmShort;
            double longPart = (distanceKm - longDistanceThreshold) * perKmLong;
            return base + shortPart + longPart;
        }
    }

    private RouteResponse.FareEstimate buildEstimate(String type, String typeJa, String typeVi,
                                                      int seats, double fare) {
        // Làm tròn đến 1000₫
        double roundedFare = Math.ceil(fare / 1000) * 1000;

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formatted = formatter.format((long) roundedFare) + "₫";

        return RouteResponse.FareEstimate.builder()
                .vehicleType(type)
                .vehicleTypeJa(typeJa)
                .vehicleTypeVi(typeVi)
                .seats(seats)
                .estimatedFare(roundedFare)
                .fareFormatted(formatted)
                .build();
    }
}
