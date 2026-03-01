package com.wolsera.wolsera_ecommerce.auth.service;

import com.wolsera.wolsera_ecommerce.auth.entity.User;
import com.wolsera.wolsera_ecommerce.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public User validate(String email, String password) {
        User user  = repo.findByEmail(email).orElseThrow(()-> new BadCredentialsException("Invalid credentials"));
        if(encoder.matches(password, user.getPassword())){
            return user;
        }
        throw new BadCredentialsException("Invalid credentials");
    }
}
