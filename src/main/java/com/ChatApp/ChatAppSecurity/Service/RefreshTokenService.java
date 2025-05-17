package com.ChatApp.ChatAppSecurity.Service;

import com.ChatApp.ChatAppSecurity.Repository.UserDataRepository;
import com.ChatApp.ChatAppSecurity.dto.CustomUserDetails;
import com.ChatApp.ChatAppSecurity.dto.RefreshTokenRequest;
import com.ChatApp.ChatAppSecurity.dto.RefreshTokenResponse;
import com.ChatApp.ChatAppSecurity.dto.UserData;
import com.ChatApp.ChatAppSecurity.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RefreshTokenService {
    @Autowired
    private UserDataRepository userDataRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public ResponseEntity<?> refreshAccessToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body(createErrorResponse("Refresh token is required"));
        }

        // Validate refresh token
        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorResponse("Invalid or expired refresh token"));
        }

        // Extract username from refresh token
        String username;
        try {
            username = jwtUtils.getUserNameFromRefreshToken(refreshToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorResponse("Invalid refresh token"));
        }

        // Find user and verify refresh token
        Optional<UserData> optionalUser = userDataRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("User not found"));
        }

        UserData user = optionalUser.get();
        if (!refreshToken.equals(user.getResetToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorResponse("Refresh token does not match"));
        }

        // Check refresh token expiry
        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createErrorResponse("Refresh token has expired"));
        }

        // Generate new access token
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtils.generateAccessTokenForSignInRequest(userDetails);

        // Return response
        RefreshTokenResponse response = new RefreshTokenResponse(newAccessToken);
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", message);
        error.put("status", false);
        return error;
    }
}

