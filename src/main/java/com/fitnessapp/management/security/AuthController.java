package com.fitnessapp.management.security;

import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.dto.UserResponseDTO;
import com.fitnessapp.management.repository.dto.UserSecurityDTO;
import com.fitnessapp.management.repository.entity.RefreshToken;
import com.fitnessapp.management.security.request.LoginRequest;
import com.fitnessapp.management.security.request.RegisterRequest;
import com.fitnessapp.management.security.response.AuthResponse;
import com.fitnessapp.management.security.token.JwtService;
import com.fitnessapp.management.security.token.RefreshTokenService;
import com.fitnessapp.management.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserService userService;
    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserService userService,
            RefreshTokenService refreshTokenService,
            JwtService jwtService,
            SessionService sessionService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.sessionService = sessionService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        UserResponseDTO userResponseDTO = userService.addUser(signUpRequest);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        AuthResponse authResponse = refreshTokenService.refreshAuthToken(request, response);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        sessionService.extractToken(request, jwtService.getTokenConfig())
                .ifPresent(token -> {
                    refreshTokenService.invalidateToken(token);
                    jwtService.blacklistToken(token);
                });
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }

    @Transactional
    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateToken(authentication.getName());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication.getName());

            ResponseCookie accessCookie = sessionService.createTokenCookie(
                    sessionService.extractSessionHeader(request),
                    accessToken,
                    jwtService.getTokenConfig());
            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

            ResponseCookie refreshCookie = sessionService.createTokenCookie(
                    sessionService.extractSessionHeader(request),
                    refreshToken.getToken(),
                    refreshTokenService.getTokenConfig());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

            UserSecurityDTO userData = userService.getUserByUsername(authentication.getName(), UserSecurityDTO.class);

            return new ResponseEntity<>(
                    new AuthResponse(
                            jwtService.extractExpiration(accessToken),
                            Date.from(refreshToken.getExpiryDate()),
                            userData
                    ),
                    HttpStatus.OK
            );
        } else {
            throw new UsernameNotFoundException("Bad request");
        }
    }
}
