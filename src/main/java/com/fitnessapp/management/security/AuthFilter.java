package com.fitnessapp.management.security;

import com.fitnessapp.management.exception.RefreshTokenExpiredException;
import com.fitnessapp.management.security.token.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final SessionService sessionService;

    @Value("${gym.app.publicRoutes}")
    private String[] publicRoutes;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String requestUri = request.getRequestURI();

            boolean isPublic = Arrays.stream(publicRoutes)
                    .map(route -> route.replaceAll("\\*", ""))
                    .anyMatch(requestUri::contains);

            if (isPublic) {
                filterChain.doFilter(request, response);
                return;
            }
            sessionService.extractToken(request, jwtService.getTokenConfig())
                    .ifPresent(token -> {
                        String username = jwtService.extractUsername(token);

                        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                            if (jwtService.validateToken(token, userDetails)) {
                                String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
                                GrantedAuthority authority = new SimpleGrantedAuthority(role);

                                UsernamePasswordAuthenticationToken auth =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails,
                                                null,
                                                Collections.singletonList(authority)
                                        );
                                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(auth);
                            }
                        }
                    });
            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException | RefreshTokenExpiredException ignored) {
        }
    }

}
