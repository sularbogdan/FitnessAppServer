package com.fitnessapp.management.security.token;

import com.fitnessapp.management.exception.InvalidRefreshTokenException;
import com.fitnessapp.management.exception.RefreshTokenExpiredException;
import com.fitnessapp.management.exception.UserNotFoundException;
import com.fitnessapp.management.repository.RefreshTokenRepository;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.dto.UserSecurityDTO;
import com.fitnessapp.management.repository.entity.RefreshToken;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.security.SessionService;
import com.fitnessapp.management.security.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SessionService sessionService;

    @Getter
    private final RefreshTokenConfig tokenConfig;

    @Transactional
    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException("User not found")))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(tokenConfig.getExpiryMs()))
                .build();

        User user = refreshToken.getUser();
        user.getRefreshTokens().add(refreshToken);

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        userRepository.save(user);

        return savedToken;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException(token, "Expired refresh token");
        }
        return token;
    }

    public AuthResponse refreshAuthToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> token = sessionService.extractToken(request, tokenConfig);

        if (token.isEmpty()) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token.get())
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        verifyExpiration(refreshToken);

        Optional<User> userOptional = userRepository.findUserByRefreshToken(refreshToken);
        User user = userOptional.orElseThrow(() -> new UserNotFoundException("User not found " + refreshToken.getUser().getUsername()));

        String username = user.getUsername();

        refreshTokenRepository.delete(refreshToken);

        String newAccessToken = jwtService.generateToken(username);
        RefreshToken newRefreshToken = createRefreshToken(username);

        response.addHeader(HttpHeaders.SET_COOKIE, sessionService
                .createTokenCookie(
                        sessionService.extractSessionHeader(request),
                        newAccessToken,
                        jwtService.getTokenConfig())
                .toString());

        response.addHeader(HttpHeaders.SET_COOKIE, sessionService
                .createTokenCookie(
                        sessionService.extractSessionHeader(request),
                        newRefreshToken.getToken(),
                        tokenConfig)
                .toString());

        return new AuthResponse(
                jwtService.extractExpiration(newAccessToken),
                Date.from(newRefreshToken.getExpiryDate()),
                newAccessToken,
                new UserSecurityDTO());
    }


    public void invalidateToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }
}
