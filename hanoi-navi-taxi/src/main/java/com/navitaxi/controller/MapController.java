package com.navitaxi.controller;

import com.navitaxi.dto.request.RouteRequest;
import com.navitaxi.dto.response.ApiResponse;
import com.navitaxi.dto.response.RouteResponse;
import com.navitaxi.service.FareCalculationService;
import com.navitaxi.service.GoogleMapsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Map Controller - API bản đồ và tính giá cước
 * 地図コントローラー - 地図・料金計算API
 */
@RestController
@RequestMapping("/api/maps")
@Tag(name = "Maps & Fare", description = "地図・料金API / API Bản đồ & Giá cước")
public class MapController {

    private final GoogleMapsService googleMapsService;
    private final FareCalculationService fareCalculationService;

    public MapController(GoogleMapsService googleMapsService,
                         FareCalculationService fareCalculationService) {
        this.googleMapsService = googleMapsService;
        this.fareCalculationService = fareCalculationService;
    }

    /**
     * POST /api/maps/route - Tính lộ trình
     * ルート計算
     */
    @PostMapping("/route")
    @Operation(summary = "Tính lộ trình / ルート計算",
               description = "Tính khoảng cách, thời gian và giá cước giữa 2 điểm")
    public ResponseEntity<ApiResponse> calculateRoute(@Valid @RequestBody RouteRequest request) {
        RouteResponse routeResponse = googleMapsService.calculateRoute(request);
        return ResponseEntity.ok(ApiResponse.success("OK", routeResponse));
    }

    /**
     * GET /api/maps/search - Tìm kiếm địa chỉ
     * 住所検索
     */
    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm địa chỉ / 住所検索",
               description = "Tìm kiếm và geocode địa chỉ")
    public ResponseEntity<ApiResponse> searchAddress(@RequestParam String query) {
        var results = googleMapsService.searchAddress(query);
        return ResponseEntity.ok(ApiResponse.success("OK", results));
    }

    /**
     * GET /api/maps/fare-estimate - Ước tính giá cước
     * 料金見積
     */
    @GetMapping("/fare-estimate")
    @Operation(summary = "Ước tính giá cước / 料金見積",
               description = "Ước tính giá cước dựa trên khoảng cách")
    public ResponseEntity<ApiResponse> fareEstimate(@RequestParam double distanceKm) {
        var estimates = fareCalculationService.calculateAllFares(distanceKm);
        return ResponseEntity.ok(ApiResponse.success("OK", estimates));
    }
}
