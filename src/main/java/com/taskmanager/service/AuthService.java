package com.taskmanager.service;

import com.taskmanager.error.exception.BadRequestException;
import com.taskmanager.model.dto.auth.JwtAuthenticationResponse;
import com.taskmanager.model.dto.auth.LoginRequest;
import com.taskmanager.model.dto.auth.UserRegistrationRequest;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.security.InMemoryTokenBlacklistService;
import com.taskmanager.security.JwtTokenProvider;
import com.taskmanager.model.entity.User;
import com.taskmanager.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final InMemoryTokenBlacklistService tokenBlacklistService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public JwtAuthenticationResponse registerUser(UserRegistrationRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("already in use", "this email address is already in use");
        }

        // Create new user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        log.info("New user registered: {}", savedUser.getName());

        // Generate JWT token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        return createAuthResponse(authentication);
    }

    public JwtAuthenticationResponse authenticateUser(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return createAuthResponse(authentication);

        } catch (AuthenticationException ex) {
            log.error("Authentication failed for user: {}", request.getEmail());
            throw new BadRequestException("Invalid credentials", "Invalid email or password");
        }
    }

    public void logout(String token) {
        Date expiry = tokenProvider.getExpirationDateFromToken(token);
        tokenBlacklistService.blacklist(token, expiry);
        SecurityContextHolder.clearContext();
        log.info("User logged out and token blacklisted successfully");
    }

    private JwtAuthenticationResponse createAuthResponse(Authentication authentication) {
        String jwt = tokenProvider.generateToken(authentication);
        Date expiryDate = tokenProvider.getExpirationDateFromToken(jwt);
        long expiresIn = expiryDate.getTime() - System.currentTimeMillis();

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return new JwtAuthenticationResponse(
                jwt,
                userPrincipal.getId(),
                userPrincipal.getUsername(),
                userPrincipal.getEmail(),
                expiresIn
        );
    }
}
