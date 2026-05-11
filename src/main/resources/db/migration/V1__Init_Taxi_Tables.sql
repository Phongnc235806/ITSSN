CREATE TABLE `users` (
  `user_id` int PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255) UNIQUE NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `phone_number` varchar(20),
  `role` varchar(20) NOT NULL COMMENT 'CUSTOMER / DRIVER / ADMIN',
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE / INACTIVE / SUSPENDED',
  `created_at` datetime,
  `updated_at` datetime
);

CREATE TABLE `driver_profiles` (
  `driver_id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int UNIQUE NOT NULL,
  `japanese_level` varchar(20) COMMENT 'N5 / N4 / N3 / N2 / N1 / BUSINESS',
  `license_number` varchar(100),
  `is_available` boolean DEFAULT true,
  `created_at` datetime,
  `updated_at` datetime
);

CREATE TABLE `vehicles` (
  `vehicle_id` int PRIMARY KEY AUTO_INCREMENT,
  `driver_id` int NOT NULL,
  `vehicle_type` varchar(20) NOT NULL COMMENT '4_SEATS / 7_SEATS',
  `brand` varchar(100),
  `license_plate` varchar(50) UNIQUE NOT NULL,
  `color` varchar(50),
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE / INACTIVE',
  `created_at` datetime,
  `updated_at` datetime
);

CREATE TABLE `ride_requests` (
  `ride_id` int PRIMARY KEY AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `driver_id` int,
  `vehicle_id` int,
  `pickup_address` varchar(255) NOT NULL,
  `pickup_lat` decimal(10,7),
  `pickup_lng` decimal(10,7),
  `destination_address` varchar(255) NOT NULL,
  `destination_lat` decimal(10,7),
  `destination_lng` decimal(10,7),
  `requested_vehicle_type` varchar(20) NOT NULL COMMENT '4_SEATS / 7_SEATS',
  `estimated_distance_km` decimal(6,2),
  `estimated_duration_min` int,
  `estimated_fare` decimal(10,2),
  `final_fare` decimal(10,2),
  `status` varchar(30) NOT NULL DEFAULT 'REQUESTED' COMMENT 'REQUESTED / SEARCHING_DRIVER / ACCEPTED / DRIVER_COMING / IN_PROGRESS / COMPLETED / CANCELLED / REJECTED',
  `cancel_reason` varchar(255),
  `requested_at` datetime,
  `accepted_at` datetime,
  `started_at` datetime,
  `completed_at` datetime,
  `cancelled_at` datetime
);

CREATE TABLE `driver_locations` (
  `location_id` int PRIMARY KEY AUTO_INCREMENT,
  `driver_id` int NOT NULL,
  `latitude` decimal(10,7) NOT NULL,
  `longitude` decimal(10,7) NOT NULL,
  `updated_at` datetime
);

CREATE TABLE `vouchers` (
  `voucher_id` int PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(50) UNIQUE NOT NULL,
  `description` varchar(255),
  `discount_type` varchar(20) NOT NULL COMMENT 'PERCENT / FIXED',
  `discount_value` decimal(10,2) NOT NULL,
  `max_discount` decimal(10,2),
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE / INACTIVE / EXPIRED',
  `created_at` datetime,
  `updated_at` datetime
);

CREATE TABLE `user_vouchers` (
  `user_voucher_id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `voucher_id` int NOT NULL,
  `ride_id` int,
  `is_used` boolean DEFAULT false,
  `used_at` datetime
);

CREATE TABLE `reviews` (
  `review_id` int PRIMARY KEY AUTO_INCREMENT,
  `ride_id` int UNIQUE NOT NULL,
  `customer_id` int NOT NULL,
  `driver_id` int NOT NULL,
  `rating` int NOT NULL COMMENT '1 to 5',
  `comment` text,
  `created_at` datetime
);

CREATE TABLE `notifications` (
  `notification_id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `ride_id` int,
  `title` varchar(100) NOT NULL,
  `message` text NOT NULL,
  `type` varchar(30) NOT NULL COMMENT 'RIDE_REQUEST / RIDE_ACCEPTED / RIDE_REJECTED / RIDE_CANCELLED / SYSTEM',
  `is_read` boolean DEFAULT false,
  `created_at` datetime
);

CREATE TABLE `admin_actions` (
  `action_id` int PRIMARY KEY AUTO_INCREMENT,
  `admin_id` int NOT NULL,
  `target_user_id` int,
  `action_type` varchar(30) NOT NULL COMMENT 'SUSPEND_USER / UNSUSPEND_USER / VIEW_REPORT / UPDATE_STATUS',
  `reason` varchar(255),
  `created_at` datetime
);

ALTER TABLE `driver_profiles` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `vehicles` ADD FOREIGN KEY (`driver_id`) REFERENCES `driver_profiles` (`driver_id`);

ALTER TABLE `ride_requests` ADD FOREIGN KEY (`customer_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `ride_requests` ADD FOREIGN KEY (`driver_id`) REFERENCES `driver_profiles` (`driver_id`);

ALTER TABLE `ride_requests` ADD FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles` (`vehicle_id`);

ALTER TABLE `driver_locations` ADD FOREIGN KEY (`driver_id`) REFERENCES `driver_profiles` (`driver_id`);

ALTER TABLE `user_vouchers` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `user_vouchers` ADD FOREIGN KEY (`voucher_id`) REFERENCES `vouchers` (`voucher_id`);

ALTER TABLE `user_vouchers` ADD FOREIGN KEY (`ride_id`) REFERENCES `ride_requests` (`ride_id`);

ALTER TABLE `reviews` ADD FOREIGN KEY (`ride_id`) REFERENCES `ride_requests` (`ride_id`);

ALTER TABLE `reviews` ADD FOREIGN KEY (`customer_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `reviews` ADD FOREIGN KEY (`driver_id`) REFERENCES `driver_profiles` (`driver_id`);

ALTER TABLE `notifications` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `notifications` ADD FOREIGN KEY (`ride_id`) REFERENCES `ride_requests` (`ride_id`);

ALTER TABLE `admin_actions` ADD FOREIGN KEY (`admin_id`) REFERENCES `users` (`user_id`);

ALTER TABLE `admin_actions` ADD FOREIGN KEY (`target_user_id`) REFERENCES `users` (`user_id`);