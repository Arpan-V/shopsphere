package com.arpan.backend.service.impl;
import com.arpan.backend.dto.auth.LoginRequest;
import com.arpan.backend.dto.auth.RegisterRequest;
import com.arpan.backend.entity.Users;
import com.arpan.backend.repository.UserRepo;
import com.arpan.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String register(RegisterRequest request) {
        Users user = Users.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
        return "User registered successfully.";
    }

    @Override
    public String login(LoginRequest request) {
        return "Login logic coming soon";
    }
}