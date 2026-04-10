package com.arpan.backend.service.impl;
import com.arpan.backend.dto.request.LoginRequest;
import com.arpan.backend.dto.request.RegisterRequest;
import com.arpan.backend.entity.Users;
import com.arpan.backend.repository.UserRepo;
import com.arpan.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepository;

    @Override
    public String register(RegisterRequest request) {
        Users user = Users.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword()) // hash later
                .role("USER")
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public String login(LoginRequest request) {
        return "Login logic coming soon";
    }
}