package com.ChatApp.ChatAppSecurity.Service;

import com.ChatApp.ChatAppSecurity.Repository.UserDataRepository;
import com.ChatApp.ChatAppSecurity.dto.CustomUserDetails;
import com.ChatApp.ChatAppSecurity.dto.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LogOutService {

    @Autowired
    private UserDataRepository userDataRepository;


    @Transactional
    public ResponseEntity<?> logout(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("User is not authenticated"));
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Optional<UserData> optionalUser = userDataRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("User not found"));
        }

        UserData user = optionalUser.get();
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userDataRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Logout successful");
        response.put("status", true);
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", message);
        error.put("status", false);
        return error;
    }
}