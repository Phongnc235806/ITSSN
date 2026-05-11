package com.navitaxi.service;

import com.navitaxi.dto.request.RouteRequest;
import com.navitaxi.dto.response.RouteResponse;
import com.google.maps.*;
import com.google.maps.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * Google Maps Service - Tích hợp Google Maps API
 * Google Maps API連携サービス
 */
@Service
public class GoogleMapsService {

    private static final Logger log = LoggerFactory.getLogger(GoogleMapsService.class);

    @Value("${google.maps.api-key}")
    private String apiKey;

    private GeoApiContext geoApiContext;

    private final FareCalculationService fareCalculationService;

    public GoogleMapsService(FareCalculationService fareCalculationService) {
        this.fareCalculationService = fareCalculationService;
    }

    @PostConstruct
    public void init() {
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    /**
     * Tính toán lộ trình giữa 2 điểm
     * 2地点間のルートを計算
     */
    public RouteResponse calculateRoute(RouteRequest request) {
        try {
            DirectionsResult result;

            // Nếu có tọa độ, dùng tọa độ; không thì dùng địa chỉ text
            if (request.getPickupLat() != null && request.getPickupLng() != null
                    && request.getDestinationLat() != null && request.getDestinationLng() != null) {
                result = DirectionsApi.newRequest(geoApiContext)
                        .origin(new com.google.maps.model.LatLng(request.getPickupLat(), request.getPickupLng()))
                        .destination(new com.google.maps.model.LatLng(request.getDestinationLat(), request.getDestinationLng()))
                        .mode(TravelMode.DRIVING)
                        .language("vi")
                        .await();
            } else {
                result = DirectionsApi.newRequest(geoApiContext)
                        .origin(request.getPickupAddress())
                        .destination(request.getDestinationAddress())
                        .mode(TravelMode.DRIVING)
                        .language("vi")
                        .await();
            }

            if (result.routes.length == 0) {
                throw new RuntimeException("Không tìm thấy lộ trình / ルートが見つかりません");
            }

            DirectionsRoute route = result.routes[0];
            DirectionsLeg leg = route.legs[0];

            // Tính khoảng cách (km) và thời gian (phút)
            double distanceKm = leg.distance.inMeters / 1000.0;
            double durationMin = leg.duration.inSeconds / 60.0;

            // Tính giá cước cho tất cả loại xe
            List<RouteResponse.FareEstimate> fareEstimates =
                    fareCalculationService.calculateAllFares(distanceKm);

            return RouteResponse.builder()
                    .pickupAddress(leg.startAddress)
                    .pickupLat(leg.startLocation.lat)
                    .pickupLng(leg.startLocation.lng)
                    .destinationAddress(leg.endAddress)
                    .destinationLat(leg.endLocation.lat)
                    .destinationLng(leg.endLocation.lng)
                    .distanceKm(Math.round(distanceKm * 100.0) / 100.0)
                    .distanceText(leg.distance.humanReadable)
                    .durationMin(Math.round(durationMin * 10.0) / 10.0)
                    .durationText(leg.duration.humanReadable)
                    .encodedPolyline(route.overviewPolyline.getEncodedPath())
                    .fareEstimates(fareEstimates)
                    .build();

        } catch (Exception e) {
            log.error("Error calculating route: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi tính toán lộ trình / ルート計算エラー: " + e.getMessage());
        }
    }

    /**
     * Tìm kiếm địa chỉ / 場所検索 (Geocoding)
     */
    public GeocodingResult[] searchAddress(String query) {
        try {
            return GeocodingApi.geocode(geoApiContext, query)
                    .language("vi")
                    .await();
        } catch (Exception e) {
            log.error("Error geocoding address: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi tìm kiếm địa chỉ / 住所検索エラー: " + e.getMessage());
        }
    }
}
