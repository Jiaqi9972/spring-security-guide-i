package me.findthepeach.springsecuritydemo.service;

import me.findthepeach.springsecuritydemo.domain.domain.dto.LoginRequest;
import me.findthepeach.springsecuritydemo.domain.domain.dto.RegisterRequest;
import me.findthepeach.springsecuritydemo.domain.entity.User;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface UserService {
    void register(RegisterRequest request);
    User getUserByUsername(String username);
    boolean validateUser(LoginRequest request);

    Map<String, String> getUserInfo(Authentication authentication);
}
