package me.findthepeach.springsecuritydemo.domain.domain.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}
