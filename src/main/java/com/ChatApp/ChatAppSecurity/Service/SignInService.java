package com.ChatApp.ChatAppSecurity.Service;

import com.ChatApp.ChatAppSecurity.Repository.UserDataRepository;
import com.ChatApp.ChatAppSecurity.dto.CustomUserDetails;
import com.ChatApp.ChatAppSecurity.dto.SignInRequest;
import com.ChatApp.ChatAppSecurity.dto.SignInResponse;
import com.ChatApp.ChatAppSecurity.dto.UserData;
import com.ChatApp.ChatAppSecurity.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SignInService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDataRepository userDataRepository;

    @Value("${spring.app.refreshTokenExpirationTime}")
    private Long refreshTokenExpirationTime;

    public ResponseEntity<?> validateUser(SignInRequest signInRequest) {
        Authentication authentication;
        try{

            // Authenticate the user
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials: " + e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Authentication error: " + e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Generate tokens
        String jwtAccessToken = jwtUtils.generateAccessTokenForSignInRequest(userDetails);
        String jwtRefreshToken = jwtUtils.generateRefreshTokenForSignInRequest(userDetails);

        // Update user data with refresh token
        Optional<UserData> optionalUser = userDataRepository.findByUsername(userDetails.getUsername());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        UserData userData = optionalUser.get();
        userData.setResetToken(jwtRefreshToken);
        userData.setResetTokenExpiry(LocalDateTime.now().plusSeconds(refreshTokenExpirationTime / 1000));
        userDataRepository.save(userData);

        // Return response
        SignInResponse signInResponse = new SignInResponse(
                signInRequest.getUsername(),
                jwtAccessToken,
                jwtRefreshToken
        );
        return ResponseEntity.ok(signInResponse);
    }
}