package com.arpan.backend.service.impl;
import com.arpan.backend.dto.ApiResponse;
import com.arpan.backend.dto.auth.AuthResponse;
import com.arpan.backend.dto.auth.LoginRequest;
import com.arpan.backend.dto.auth.RegisterRequest;
import com.arpan.backend.entity.RefreshToken;
import com.arpan.backend.entity.Users;
import com.arpan.backend.entity.VerificationToken;
import com.arpan.backend.exception.AuthException;
import com.arpan.backend.exception.ConflictException;
import com.arpan.backend.repository.RefreshTokenRepository;
import com.arpan.backend.repository.UserRepo;
import com.arpan.backend.repository.VerificationTokenRepository;
import com.arpan.backend.service.AuthService;
import com.arpan.backend.service.CustomUserDetailsService;
import com.arpan.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailsService userDetailsService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public ApiResponse<String> register(RegisterRequest request) {

        logger.info("Register request received for username: {}", request.getUsername());

        // 🔴 Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ConflictException("Username already exists");
        }

        // 🔴 Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        // 🟢 If all checks pass, create user
        Users user = Users.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .enabled(false)
                .build();

        userRepository.save(user);

        // 🔐 Generate token
        String token = UUID.randomUUID().toString();

// 💾 Save token
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 2))) // 2h
                .build();

        verificationTokenRepository.save(verificationToken);

        String link = "http://localhost:8080/api/auth/verify?token=" + token;

        emailService.sendAlert(
                user.getEmail(),
                "Verify your account",
                "Click the link to verify your account for shopsphere:\n" + link
        );

        return new ApiResponse<>(true, "User registered successfully", null);
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {
        try {
            logger.info("Login attempt for username: {}", request.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            Users user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AuthException("User does not exist"));
            if (!user.isEnabled()) {
                throw new AuthException("Please verify your email first");
            }

            String accessToken = jwtService.generateAccessToken(user.getUsername());
            String refreshToken = jwtService.generateRefreshToken(user.getUsername());

            // save refresh token
            RefreshToken tokenEntity = RefreshToken.builder()
                    .token(refreshToken)
                    .expiryDate(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 7)))
                    .revoked(false)
                    .user(user)
                    .build();

            refreshTokenRepository.save(tokenEntity);

            return new ApiResponse<>(true, "Login successful",
                    new AuthResponse(accessToken, refreshToken));

        } catch (Exception e) {
            logger.error("Login failed for username: {}", request.getUsername());
            return new ApiResponse<>(false, "Invalid credentials", null);
        }
    }

    @Override
    public ApiResponse<AuthResponse> refresh(String refreshToken) {

        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthException("Invalid refresh token"));

        // 🚨 Detect reuse (important for security)
        if (tokenEntity.isRevoked()) {
            throw new AuthException("Refresh Token reuse detected, possible attack.");
        }

        String username = jwtService.extractUserName(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.validateRefreshToken(refreshToken, userDetails)) {
            throw new AuthException("Invalid refresh token");
        }

        // 🔴 STEP 1: Revoke old refresh token
        tokenEntity.setRevoked(true);
        refreshTokenRepository.save(tokenEntity);

        // 🟢 STEP 2: Generate new refresh token
        String newRefreshToken = jwtService.generateRefreshToken(username);

        // 🟢 STEP 3: Save new refresh token in DB
        RefreshToken newTokenEntity = RefreshToken.builder()
                .token(newRefreshToken)
                .expiryDate(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 7)))
                .revoked(false)
                .user(tokenEntity.getUser())
                .build();

        refreshTokenRepository.save(newTokenEntity);

        // 🟢 STEP 4: Generate new access token
        String newAccessToken = jwtService.generateAccessToken(username);

        // 🟢 STEP 5: Return both tokens
        return new ApiResponse<>(true, "Token refreshed",
                new AuthResponse(newAccessToken, newRefreshToken));
    }

    @Override
    public ApiResponse<String> logout(String refreshToken) {

        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthException("Invalid refresh token"));

        tokenEntity.setRevoked(true);
        refreshTokenRepository.save(tokenEntity);

        return new ApiResponse<>(true, "Logged out successfully", null);
    }

    @Override
    public String verify(String token) {

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        // ⏰ Check expiry
        if (verificationToken.getExpiryDate().before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        Users user = verificationToken.getUser();

        user.setEnabled(true);
        userRepository.save(user);

        return "Account verified successfully";
    }

    @Override
    public String resendVerification(String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ❌ already verified
        if (user.isEnabled()) {
            throw new RuntimeException("User already verified");
        }
        // 🔍 get existing token (if any)
        VerificationToken existingToken = verificationTokenRepository.findByUser(user)
                .orElse(null);

        Date now = new Date();

        // ⛔ RATE LIMIT CHECK (2 minutes = 120000 ms)
        if (existingToken != null && existingToken.getLastSentAt() != null) {
            long diff = now.getTime() - existingToken.getLastSentAt().getTime();

            if (diff < 1000 * 60 * 2) {
                long remaining = (1000 * 60 * 2 - diff) / 1000;
                throw new RuntimeException(
                        "Please wait " + remaining + " seconds before requesting again"
                );
            }
            // ❌ optional: delete old token (recommended)
            verificationTokenRepository.findByUser(user)
                    .ifPresent(verificationTokenRepository::delete);
        }
        // 🔐 generate new token
        String token = UUID.randomUUID().toString();

        // 💾 save new token
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24)))
                .build();

        verificationTokenRepository.save(verificationToken);

        // 📧 send email again
        String link = "http://localhost:8080/api/auth/verify?token=" + token;

        emailService.sendAlert(
                user.getEmail(),
                "Resend Verification",
                "Click this link to verify your account:\n" + link
        );

        return "Verification email resent successfully";
    }
}