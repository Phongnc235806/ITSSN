package com.navitaxi.controller;

import com.navitaxi.dto.request.BookingRequest;
import com.navitaxi.dto.response.ApiResponse;
import com.navitaxi.model.RideRequest;
import com.navitaxi.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Booking Controller - API đặt xe
 * 配車コントローラー - 配車API
 */
@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "配車API / API Đặt xe")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * POST /api/bookings - Tạo yêu cầu đặt xe
     * 配車リクエスト作成
     */
    @PostMapping
    @Operation(summary = "Đặt xe / 配車", description = "Tạo yêu cầu đặt xe mới")
    public ResponseEntity<ApiResponse> createBooking(
            @Valid @RequestBody BookingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        RideRequest ride = bookingService.createBooking(request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(
                "Đặt xe thành công! Đang tìm tài xế... / 配車リクエストが送信されました。ドライバーを探しています...",
                ride.getRideId()));
    }

    /**
     * GET /api/bookings/{id} - Lấy thông tin chuyến đi
     * 乗車情報取得
     */
    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết chuyến đi / 乗車詳細")
    public ResponseEntity<ApiResponse> getRide(@PathVariable Integer id) {
        RideRequest ride = bookingService.getRideById(id);
        return ResponseEntity.ok(ApiResponse.success("OK", ride));
    }
    /**
     * API Hủy chuyến đi / 配車リクエストをキャンセル
     * Đường dẫn: PUT http://localhost:8080/api/bookings/cancel/{id}
     */
    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Integer id) {
        try {
            RideRequest canceledRide = bookingService.cancelBooking(id);
            return ResponseEntity.ok(canceledRide);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
