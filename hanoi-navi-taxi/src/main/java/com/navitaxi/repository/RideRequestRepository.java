package com.navitaxi.repository;

import com.navitaxi.model.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, Integer> {

    List<RideRequest> findByCustomerUserIdOrderByRequestedAtDesc(Integer customerId);

    List<RideRequest> findByDriverUserIdOrderByRequestedAtDesc(Integer driverId);

    List<RideRequest> findByStatus(String status);
}
