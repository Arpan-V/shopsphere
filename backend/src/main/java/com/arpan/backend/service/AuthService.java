package com.arpan.backend.service;

import com.arpan.backend.dto.request.LoginRequest;
import com.arpan.backend.dto.request.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);
    String login(LoginRequest request);
}