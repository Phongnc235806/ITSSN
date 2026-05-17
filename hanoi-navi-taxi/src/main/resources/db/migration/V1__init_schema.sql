-- =============================================
-- Hanoi Navi-Taxi / ハノイ・ナビタクシー
-- Database Schema - Sprint 1
-- V1: Initial Schema based on ERD
-- =============================================

-- 1. Roles table
CREATE TABLE roles (
    role_id   INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Users table
CREATE TABLE users (
    user_id       INT AUTO_INCREMENT PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    phone_number  VARCHAR(20),
    avatar_url    VARCHAR(512),
    address       VARCHAR(255),
    role          VARCHAR(50) NOT NULL,
    status        VARCHAR(30) DEFAULT 'ACTIVE',
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_email (email),
    INDEX idx_users_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Driver Profiles table
CREATE TABLE driver_profiles (
    driver_id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id        INT NOT NULL UNIQUE,
    japanese_level VARCHAR(20),
    driving_experience_years INT,
    license_number VARCHAR(50),
    is_available   BOOLEAN DEFAULT FALSE,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_driver_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Vehicles table
CREATE TABLE vehicles (
    vehicle_id   INT AUTO_INCREMENT PRIMARY KEY,
    driver_id    INT NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,
    brand        VARCHAR(100),
    color        VARCHAR(50),
    status       VARCHAR(20) DEFAULT 'ACTIVE',
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_vehicle_driver FOREIGN KEY (driver_id) REFERENCES driver_profiles(driver_id) ON DELETE CASCADE,
    INDEX idx_vehicle_type (vehicle_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Ride Requests table
CREATE TABLE ride_requests (
    ride_id                INT AUTO_INCREMENT PRIMARY KEY,
    customer_id            INT NOT NULL,
    driver_id              INT,
    vehicle_id             INT,
    pickup_address         VARCHAR(255),
    pickup_lat             DECIMAL(10,7),
    pickup_lng             DECIMAL(10,7),
    destination_address    VARCHAR(255),
    destination_lat        DECIMAL(10,7),
    destination_lng        DECIMAL(10,7),
    requested_vehicle_type VARCHAR(20),
    estimated_distance_km  DECIMAL(10,2),
    estimated_duration_min DECIMAL(10,2),
    estimated_fare         DECIMAL(15,2),
    final_fare             DECIMAL(15,2),
    status                 VARCHAR(30) DEFAULT 'PENDING',
    cancel_reason          VARCHAR(255),
    requested_at           DATETIME DEFAULT CURRENT_TIMESTAMP,
    accepted_at            DATETIME,
    status_at              DATETIME,
    completed_at           DATETIME,
    cancelled_at           DATETIME,
    CONSTRAINT fk_ride_customer FOREIGN KEY (customer_id) REFERENCES users(user_id),
    CONSTRAINT fk_ride_driver FOREIGN KEY (driver_id) REFERENCES users(user_id),
    CONSTRAINT fk_ride_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id),
    INDEX idx_ride_status (status),
    INDEX idx_ride_customer (customer_id),
    INDEX idx_ride_driver (driver_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Driver Locations table
CREATE TABLE driver_locations (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    driver_id   INT NOT NULL,
    latitude    DECIMAL(10,7),
    longitude   DECIMAL(10,7),
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_location_driver FOREIGN KEY (driver_id) REFERENCES driver_profiles(driver_id) ON DELETE CASCADE,
    INDEX idx_driver_location (driver_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Admin Actions table
CREATE TABLE admin_actions (
    action_id      INT AUTO_INCREMENT PRIMARY KEY,
    admin_id       INT NOT NULL,
    target_user_id INT,
    action_type    VARCHAR(50) NOT NULL,
    reason         VARCHAR(255),
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_admin_action_admin FOREIGN KEY (admin_id) REFERENCES users(user_id),
    CONSTRAINT fk_admin_action_target FOREIGN KEY (target_user_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Vouchers table
CREATE TABLE vouchers (
    voucher_id     INT AUTO_INCREMENT PRIMARY KEY,
    code           VARCHAR(50) NOT NULL UNIQUE,
    description    VARCHAR(255),
    discount_type  VARCHAR(20) NOT NULL,
    discount_value DECIMAL(10,2) NOT NULL,
    max_discount   DECIMAL(15,2),
    start_date     DATETIME,
    end_date       DATETIME,
    status         VARCHAR(30) DEFAULT 'ACTIVE',
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_vouchers_status_dates (status, start_date, end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. User Vouchers table
CREATE TABLE user_vouchers (
    user_voucher_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    voucher_id      INT NOT NULL,
    ride_id         INT,
    is_used         BOOLEAN DEFAULT FALSE,
    used_at         DATETIME,
    CONSTRAINT fk_uv_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_uv_voucher FOREIGN KEY (voucher_id) REFERENCES vouchers(voucher_id),
    CONSTRAINT fk_uv_ride FOREIGN KEY (ride_id) REFERENCES ride_requests(ride_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. Reviews table
CREATE TABLE reviews (
    review_id   INT AUTO_INCREMENT PRIMARY KEY,
    ride_id     INT NOT NULL,
    reviewer_id INT NOT NULL,
    reviewee_id INT NOT NULL,
    rating      INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment     TEXT,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_ride FOREIGN KEY (ride_id) REFERENCES ride_requests(ride_id),
    CONSTRAINT fk_review_reviewer FOREIGN KEY (reviewer_id) REFERENCES users(user_id),
    CONSTRAINT fk_review_reviewee FOREIGN KEY (reviewee_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 11. Notifications table
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    ride_id         INT,
    title           VARCHAR(100),
    message         VARCHAR(255),
    type            VARCHAR(50),
    is_read         BOOLEAN DEFAULT FALSE,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_notification_ride FOREIGN KEY (ride_id) REFERENCES ride_requests(ride_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
