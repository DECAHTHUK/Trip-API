package ru.tinkoff.lab.tripAPI.security.models;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
