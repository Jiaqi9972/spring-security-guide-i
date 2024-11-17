package me.findthepeach.springsecuritydemo.service.impl;

import lombok.RequiredArgsConstructor;
import me.findthepeach.springsecuritydemo.repository.UserRepository;
import me.findthepeach.springsecuritydemo.domain.domain.dto.LoginRequest;
import me.findthepeach.springsecuritydemo.domain.domain.dto.RegisterRequest;
import me.findthepeach.springsecuritydemo.domain.entity.User;
import me.findthepeach.springsecuritydemo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public boolean validateUser(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        return user != null &&
                passwordEncoder.matches(request.getPassword(), user.getPassword());
    }

    @Override
    public Map<String, String> getUserInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", user.getUsername());
        userInfo.put("role", user.getRole());
        return userInfo;
    }
}
