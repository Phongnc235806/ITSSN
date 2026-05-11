-- Thêm khách hàng người Nhật
INSERT INTO users (email, password_hash, full_name, phone_number, role, status, created_at) 
VALUES ('sakura@hust.edu.vn', 'pass123', 'Sato Sakura (佐藤 さくら)', '09012345678', 'CUSTOMER', 'ACTIVE', NOW());

-- Thêm tài xế người Nhật
INSERT INTO users (email, password_hash, full_name, phone_number, role, status, created_at) 
VALUES ('ichiro@hust.edu.vn', 'pass456', 'Tanaka Ichiro (田中 一郎)', '08098765432', 'DRIVER', 'ACTIVE', NOW());

-- Thêm hồ sơ tài xế cho Tanaka
INSERT INTO driver_profiles (user_id, japanese_level, license_number, is_available, created_at) 
VALUES (2, 'N1', 'JP-123456', true, NOW());