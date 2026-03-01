package com.wolsera.wolsera_ecommerce.auth.controller;

import com.wolsera.wolsera_ecommerce.auth.dto.AuthRequest;
import com.wolsera.wolsera_ecommerce.auth.dto.AuthResponse;
import com.wolsera.wolsera_ecommerce.auth.dto.RegisterRequest;
import com.wolsera.wolsera_ecommerce.auth.entity.User;
import com.wolsera.wolsera_ecommerce.auth.security.JwtUtil;
import com.wolsera.wolsera_ecommerce.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private UserService service;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody RegisterRequest req) {
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setPhone(req.getPhone());
        service.register(user);
        return "Signup Successful. Please Login.";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        service.validate(req.getEmail(), req.getPassword());
        String token = jwtUtil.generateToken(req.getEmail(), "USER");
        return new AuthResponse(token);
    }

}
