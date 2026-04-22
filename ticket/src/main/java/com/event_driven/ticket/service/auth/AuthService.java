package com.event_driven.ticket.service.auth;


import com.event_driven.ticket.dto.auth.AuthResponse;
import com.event_driven.ticket.dto.auth.JwtDTO;
import com.event_driven.ticket.dto.auth.LoginRequest;
import com.event_driven.ticket.dto.auth.RegisterRequest;
import com.event_driven.ticket.model.User;
import com.event_driven.ticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RateLimiterService rateLimiterService;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        if(!rateLimiterService.isAllowed("reg:limit:" + request.username(), 5, 3600)){
            throw new RuntimeException("Too many registration attempts. Try later.");
        }

        if(userRepository.existsByUsername(request.username())){
            throw new RuntimeException("Username is already taken.");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email is already registered");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .build();

        User savedUser = userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(new JwtDTO(savedUser.getId(), savedUser.getUsername())), "Bearer", jwtService.getExpirationTime());
    }

    public AuthResponse login(LoginRequest request) {

        if (!rateLimiterService.isAllowed("login:limit:" + request.username(), 5, 300)) {
            throw new RuntimeException("Too many login attempts. Try again in 5 minutes.");
        }

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid username or password."));

        if(!encoder.matches(request.password(), user.getPassword())){
            throw new RuntimeException("Invalid username or password.");
        }

        return new  AuthResponse(jwtService.generateToken(new JwtDTO(user.getId(), user.getUsername())), "Bearer", jwtService.getExpirationTime());
    }

}
