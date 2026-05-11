package com.navitaxi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navitaxi.dto.request.LoginRequest;
import com.navitaxi.dto.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Auth Controller Tests - テスト
 * Tests for registration and login API endpoints
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerCustomer_Success() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .fullName("Test User")
                .email("test_" + System.currentTimeMillis() + "@example.com")
                .password("Test@1234")
                .confirmPassword("Test@1234")
                .phoneNumber("0901234567")
                .role("CUSTOMER")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"))
                .andExpect(jsonPath("$.data.redirectUrl").value("/customer/home"));
    }

    @Test
    void registerDriver_Success() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .fullName("Driver Test")
                .email("driver_" + System.currentTimeMillis() + "@example.com")
                .password("Test@1234")
                .confirmPassword("Test@1234")
                .phoneNumber("0909876543")
                .role("DRIVER")
                .japaneseLevel("N3")
                .licenseNumber("DL-12345")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.role").value("DRIVER"))
                .andExpect(jsonPath("$.data.redirectUrl").value("/driver/home"));
    }

    @Test
    void register_DuplicateEmail_Fails() throws Exception {
        String email = "dup_" + System.currentTimeMillis() + "@example.com";
        
        RegisterRequest request = RegisterRequest.builder()
                .fullName("User 1").email(email)
                .password("Test@1234").confirmPassword("Test@1234")
                .role("CUSTOMER").build();

        // First registration
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Second registration with same email
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_WeakPassword_Fails() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .fullName("Weak Pass")
                .email("weak_" + System.currentTimeMillis() + "@example.com")
                .password("123")
                .confirmPassword("123")
                .role("CUSTOMER").build();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_InvalidCredentials_Fails() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("nonexistent@example.com")
                .password("WrongPass@123")
                .build();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
