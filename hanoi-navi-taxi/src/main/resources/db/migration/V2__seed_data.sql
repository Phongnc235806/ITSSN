-- =============================================
-- Seed Data - Default Roles and Admin User
-- =============================================

-- Insert default roles
INSERT INTO roles (role_name) VALUES ('CUSTOMER');
INSERT INTO roles (role_name) VALUES ('DRIVER');
INSERT INTO roles (role_name) VALUES ('ADMIN');

-- Insert default admin user
-- Password: Admin@123 (BCrypt encoded)
INSERT INTO users (email, password_hash, full_name, phone_number, role, status)
VALUES ('admin@navitaxi.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Admin', '0901234567', 'ADMIN', 'ACTIVE');

-- Insert sample voucher
INSERT INTO vouchers (code, description, discount_type, discount_value, max_discount, start_date, end_date, status)
VALUES ('WELCOME2024', 'Giảm giá chào mừng / ウェルカム割引', 'PERCENTAGE', 10.00, 50000.00, '2024-01-01', '2025-12-31', 'ACTIVE');
