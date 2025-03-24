package com.fitnessapp.management.security.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnessapp.management.repository.BlacklistedAccessTokenRepository;
import com.fitnessapp.management.repository.dto.UserSecurityDTO;
import com.fitnessapp.management.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class JwtService {

    @Value("${gym.app.jwt.secret}")
    private String secret;

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final BlacklistedAccessTokenRepository blacklistedAccessTokenRepository;

    @Getter
    private final AccessTokenConfig tokenConfig;

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        boolean isUsernameValid = username.equals(userDetails.getUsername());
        boolean isTokenExpired = isTokenExpired(token);
        boolean isTokenBlacklisted = isTokenBlacklisted(token);
        return isUsernameValid && !isTokenExpired && !isTokenBlacklisted;
    }

    public String generateToken(String username) {
        Map<String, Object> claims = objectMapper.convertValue(
                userService.getUserByUsername(username, UserSecurityDTO.class),
                HashMap.class);
        return createToken(claims, username);
    }

    private Boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate.before(new Date());
    }

    private Boolean isTokenBlacklisted(String token) {
        return blacklistedAccessTokenRepository.findByToken(token).isPresent();
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenConfig.getExpiryMs()))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Scheduled(cron = "0 0 12 * * ?")
    private void deleteExpiredTokens() {
        try {
            blacklistedAccessTokenRepository.deleteAllByExpiryLessThan(new Date());
        } catch (Exception ignored) {

        }
    }
}
