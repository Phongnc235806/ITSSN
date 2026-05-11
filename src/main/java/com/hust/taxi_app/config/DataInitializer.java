package com.hust.taxi_app.config;

import com.hust.taxi_app.repository.UserRepository;
import com.hust.taxi_app.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                // --- CẤP ĐỘ 1: DỮ LIỆU HỆ THỐNG (ADMIN) ---
                User admin = new User(null, "admin@taxi.jp", "admin123", 
                                     "System Admin", "03-0000-0000", "ADMIN", "ACTIVE", null);

                // --- CẤP ĐỘ 2: DỮ LIỆU MẪU NGƯỜI NHẬT (TEST) ---
                User customer = new User(null, "sato@gmail.com", "pass123", 
                                        "Sato Sakura (佐藤 さくら)", "090-1111-2222", "CUSTOMER", "ACTIVE", null);

                // --- CẤP ĐỘ 3: DỮ LIỆU NHIỀU (DUMMY DATA ĐỂ DEMO) ---
                System.out.println("Đang khởi tạo dữ liệu mẫu người Nhật...");
                
                User driver1 = new User(null, "tanaka@driver.jp", "pass456", 
                                       "Tanaka Ichiro (田中 一郎)", "080-5555-6666", "DRIVER", "ACTIVE", null);
                
                User driver2 = new User(null, "suzuki@driver.jp", "pass456", 
                                       "Suzuki Kenji (鈴木 健二)", "080-7777-8888", "DRIVER", "ACTIVE", null);

                // Lưu tất cả vào Database
                userRepository.saveAll(List.of(admin, customer, driver1, driver2));
                
                System.out.println("=> Khởi tạo dữ liệu thành công!");
            } else {
                System.out.println("=> Database đã có dữ liệu, bỏ qua bước khởi tạo.");
            }
        };
    }
}