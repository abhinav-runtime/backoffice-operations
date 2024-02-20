package com.backoffice.operations.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.web.filter.OncePerRequestFilter;

import com.backoffice.operations.entity.User;
import com.backoffice.operations.repository.UserRepository;
import com.backoffice.operations.security.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TokenRefreshFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;

    public TokenRefreshFilter(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String userEmail = jwtTokenProvider.getUsername(token);

            if (!userRepository.existsByUsername(userEmail)) {
                // User not found, return 401 Unauthorized
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                return;
            }

            if (jwtTokenProvider.isTokenExpired(token)) {
                // Token is expired, generate a new one with extended validity
                Optional<User> userDetails = userRepository.findByEmail(userEmail);
                String newToken = jwtTokenProvider.generateToken(userDetails.get());

                // Add the new token to the response header
                response.addHeader("Authorization", "Bearer " + newToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }


}

