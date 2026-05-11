package com.navitaxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ハノイ・ナビタクシー - Hanoi Navi-Taxi Application
 * Main entry point for the Spring Boot application.
 * 
 * Ứng dụng kết nối khách hàng Nhật Bản tại Hà Nội với tài xế taxi biết tiếng Nhật.
 */
@SpringBootApplication
public class NaviTaxiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaviTaxiApplication.class, args);
    }
}
