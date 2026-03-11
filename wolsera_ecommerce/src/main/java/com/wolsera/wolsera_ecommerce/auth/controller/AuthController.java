package com.wolsera.wolsera_ecommerce.auth.controller;

import com.wolsera.wolsera_ecommerce.auth.dto.*;
import com.wolsera.wolsera_ecommerce.auth.entity.User;
import com.wolsera.wolsera_ecommerce.auth.security.JwtUtil;
import com.wolsera.wolsera_ecommerce.auth.service.PasswordResetService;
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
    @Autowired
    private PasswordResetService passwordResetService;

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

    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String token = passwordResetService.createResetToken(request.getEmail());
        return "Password reset token: " + token;
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        );
        return "Password reset successful";
    }

}
