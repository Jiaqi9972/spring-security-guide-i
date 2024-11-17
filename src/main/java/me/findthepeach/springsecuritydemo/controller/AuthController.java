package me.findthepeach.springsecuritydemo.controller;

import lombok.RequiredArgsConstructor;
import me.findthepeach.springsecuritydemo.domain.domain.dto.LoginRequest;
import me.findthepeach.springsecuritydemo.domain.domain.dto.RegisterRequest;
import me.findthepeach.springsecuritydemo.service.UserService;
import me.findthepeach.springsecuritydemo.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            if (userService.validateUser(request)) {
                String token = JwtUtil.generateJwt(request.getUsername());
                return ResponseEntity.ok(new HashMap<String, String>() {{
                    put("token", token);
                    put("username", request.getUsername());
                }});
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        try {
            Map<String, String> userInfo = userService.getUserInfo(authentication);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get user info: " + e.getMessage()));
        }
    }
}
