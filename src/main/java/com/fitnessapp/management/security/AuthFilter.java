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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

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
            sessionService.extractToken(request, jwtService.getTokenConfig())
                    .ifPresent(token -> {
                        if (Arrays.stream(publicRoutes)
                                .map(route -> route.replaceAll("\\*", ""))
                                .anyMatch(route -> request.getRequestURI().contains(route))) {
                            return;
                        }

                        String username = jwtService.extractUsername(token);

                        if (username != null) {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            if (jwtService.validateToken(token, userDetails)) {
                                UsernamePasswordAuthenticationToken authenticationToken =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails,
                                                null,
                                                userDetails.getAuthorities());
                                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                                        .buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            }
                        }
                    });

            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException | RefreshTokenExpiredException ignored) {
        }
    }
}
