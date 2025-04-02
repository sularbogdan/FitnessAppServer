package com.fitnessapp.management.security;

import com.fitnessapp.management.security.token.TokenConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    @Value("${gym.app.sessionIdHeader}")
    private String sessionIDHeader;

    public ResponseCookie createTokenCookie(String sessionId, String token, TokenConfig tokenConfig) {
        return ResponseCookie.from(getIdentifier(tokenConfig.getType(), sessionId), token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path(tokenConfig.getPath())
                .maxAge(tokenConfig.getExpirySeconds())
                .build();
    }

    public Optional<String> extractToken(HttpServletRequest request, TokenConfig tokenConfig) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        }

        return getTokenFromCookies(request.getCookies(), tokenConfig.getType());
    }

    private Optional<String> getTokenFromCookies(Cookie[] cookies, String identifier) {
        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(identifier)) {
                return Optional.of(cookie.getValue());
            }
        }

        return Optional.empty();
    }

    public String getIdentifier(String type, String sessionId) {
        return sessionId != null ? type + sessionId : type;
    }

    public String extractSessionHeader(HttpServletRequest request) {
        return request.getHeader(sessionIDHeader);
    }
}
