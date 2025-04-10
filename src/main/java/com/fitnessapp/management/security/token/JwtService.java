package com.fitnessapp.management.security.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnessapp.management.repository.BlacklistedAccessTokenRepository;
import com.fitnessapp.management.repository.dto.UserSecurityDTO;
import com.fitnessapp.management.repository.entity.BlacklistedAccessToken;
import com.fitnessapp.management.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
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
public class JwtService {

    @Value("${gym.app.jwt.secret}")
    private String secret;

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final BlacklistedAccessTokenRepository blacklistedAccessTokenRepository;

    @Getter
    private final AccessTokenConfig tokenConfig;

    public JwtService(ObjectMapper objectMapper,
                      @Lazy UserService userService,
                      BlacklistedAccessTokenRepository blacklistedAccessTokenRepository,
                      AccessTokenConfig tokenConfig) {
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.blacklistedAccessTokenRepository = blacklistedAccessTokenRepository;
        this.tokenConfig = tokenConfig;
    }

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
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && !isTokenBlacklisted(token);
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public String generateToken(String username) {
        Map<String, Object> claims = objectMapper.convertValue(
                userService.getUserByUsername(username, UserSecurityDTO.class),
                HashMap.class
        );

        UserDetails userDetails = userService.loadUserByUsername(username);
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_CLIENT");

        claims.put("role", role);

        return createToken(claims, username);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Boolean isTokenBlacklisted(String token) {
        return blacklistedAccessTokenRepository.findByToken(token).isPresent();
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
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
        } catch (Exception ignored) {}
    }

    public void blacklistToken(String token) {
        Date expiry = extractExpiration(token);
        BlacklistedAccessToken blacklistedToken = new BlacklistedAccessToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiry(expiry);
        blacklistedAccessTokenRepository.save(blacklistedToken);
    }
}
