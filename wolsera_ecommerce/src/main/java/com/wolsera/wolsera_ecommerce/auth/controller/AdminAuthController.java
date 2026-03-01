package com.wolsera.wolsera_ecommerce.auth.controller;

import com.wolsera.wolsera_ecommerce.auth.dto.AdminLoginRequest;
import com.wolsera.wolsera_ecommerce.auth.entity.User;
import com.wolsera.wolsera_ecommerce.auth.security.JwtUtil;
import com.wolsera.wolsera_ecommerce.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${admin.secret.key}")
    private String adminSecretKey;

    @PostMapping("/login")
    public String adminLogin(@Valid @RequestBody AdminLoginRequest request) {

        User user = userService.validate(
                request.getEmail(),
                request.getPassword()
        );

        if(!adminSecretKey.equals(request.getSecretKey())){
            throw new BadCredentialsException("Invalid admin secret Key");
        }

        return jwtUtil.generateToken(user.getEmail(), "ADMIN");
    }
}
