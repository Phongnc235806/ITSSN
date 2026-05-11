package com.hust.taxi_app.repository;

import com.hust.taxi_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Spring Boot sẽ tự động tạo các hàm save(), findAll(), count()...
}