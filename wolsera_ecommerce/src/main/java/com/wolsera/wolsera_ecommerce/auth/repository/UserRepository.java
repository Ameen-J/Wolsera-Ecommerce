package com.wolsera.wolsera_ecommerce.auth.repository;

import com.wolsera.wolsera_ecommerce.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
