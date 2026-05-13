package com.navitaxi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Page Controller - Điều hướng trang Thymeleaf
 * ページコントローラー - Thymeleafページルーティング
 */
@Controller
public class PageController {

    /** Trang chủ - redirect đến đăng nhập */
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    /** Màn hình Đăng nhập (ID 1) / ログイン画面 */
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    /** Màn hình Đăng ký (ID 2) / 新規登録画面 */
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    /** Trang chủ Khách hàng + Tìm lộ trình (ID 3) / お客様ホーム */
    @GetMapping("/customer/home")
    public String customerHome() {
        return "customer/home";
    }

    /** Trang Đặt xe (ID 4) / 配車画面 */
    @GetMapping("/customer/booking")
    public String customerBooking() {
        return "customer/booking";
    }

    /** Trang chủ Tài xế / ドライバーホーム */
    @GetMapping("/driver/home")
    public String driverHome() {
        return "driver/home";
    }

    /** Trang Quản trị / 管理者ダッシュボード */
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }
}
