package com.navitaxi.service;

import com.navitaxi.dto.request.BookingRequest;
import com.navitaxi.exception.BadRequestException;
import com.navitaxi.model.RideRequest;
import com.navitaxi.model.User;
import com.navitaxi.repository.RideRequestRepository;
import com.navitaxi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Booking Service - Xử lý đặt xe
 * 配車サービス - 配車リクエストの処理
 */
@Service
public class BookingService {

    private final RideRequestRepository rideRequestRepository;
    private final UserRepository userRepository;
    private final FareCalculationService fareCalculationService;

    public BookingService(RideRequestRepository rideRequestRepository,
                          UserRepository userRepository,
                          FareCalculationService fareCalculationService) {
        this.rideRequestRepository = rideRequestRepository;
        this.userRepository = userRepository;
        this.fareCalculationService = fareCalculationService;
    }

    /**
     * Tạo yêu cầu đặt xe mới / 新しい配車リクエストを作成
     */
    @Transactional
    public RideRequest createBooking(BookingRequest request, String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new BadRequestException(
                    "Không tìm thấy tài khoản / アカウントが見つかりません"));

        // Tính giá cước
        double fare = fareCalculationService.calculateFareForType(
                request.getEstimatedDistanceKm(), request.getVehicleType());

        RideRequest rideRequest = RideRequest.builder()
                .customer(customer)
                .pickupAddress(request.getPickupAddress())
                .pickupLat(BigDecimal.valueOf(request.getPickupLat()))
                .pickupLng(BigDecimal.valueOf(request.getPickupLng()))
                .destinationAddress(request.getDestinationAddress())
                .destinationLat(BigDecimal.valueOf(request.getDestinationLat()))
                .destinationLng(BigDecimal.valueOf(request.getDestinationLng()))
                .requestedVehicleType(request.getVehicleType().toUpperCase())
                .estimatedDistanceKm(BigDecimal.valueOf(request.getEstimatedDistanceKm()))
                .estimatedDurationMin(BigDecimal.valueOf(request.getEstimatedDurationMin()))
                .estimatedFare(BigDecimal.valueOf(fare))
                .status("PENDING")
                .build();

        return rideRequestRepository.save(rideRequest);
    }

    /**
     * Lấy danh sách chuyến đi của khách hàng
     */
    public List<RideRequest> getCustomerRides(Integer customerId) {
        return rideRequestRepository.findByCustomerUserIdOrderByRequestedAtDesc(customerId);
    }

    /**
     * Lấy chuyến đi theo ID
     */
    public RideRequest getRideById(Integer rideId) {
        return rideRequestRepository.findById(rideId)
                .orElseThrow(() -> new BadRequestException(
                    "Không tìm thấy chuyến đi / 乗車リクエストが見つかりません"));
    }
    /**
     * Hủy chuyến đi / 配車リクエストをキャンセル
     */
    @Transactional
    public RideRequest cancelBooking(Integer rideId) {
        // 1. Tìm chuyến đi dựa vào ID (Sử dụng hàm getRideById đã có sẵn trong file)
        RideRequest rideRequest = getRideById(rideId);
        
        // 2. Logic kiểm tra: Nếu chuyến đi đã hoàn thành hoặc đã hủy trước đó thì không cho hủy nữa
        if ("COMPLETED".equals(rideRequest.getStatus()) || "CANCELLED".equals(rideRequest.getStatus())) {
            throw new BadRequestException("Không thể hủy chuyến đi đã kết thúc / 終了したリクエストはキャンセルできません");
        }

        // 3. Cập nhật trạng thái thành CANCELLED
        rideRequest.setStatus("CANCELLED");
        
        // 4. Lưu lại vào Database thông qua repository mà bạn vừa thấy trong ảnh
        return rideRequestRepository.save(rideRequest);
    }
}

