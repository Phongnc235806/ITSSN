package com.navitaxi.repository;

import com.navitaxi.model.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DriverProfileRepository extends JpaRepository<DriverProfile, Integer> {

    Optional<DriverProfile> findByUserUserId(Integer userId);
}
