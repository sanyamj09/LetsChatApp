package com.ChatApp.ChatAppSecurity.Controllers;

import com.ChatApp.ChatAppSecurity.Service.RefreshTokenService;
import com.ChatApp.ChatAppSecurity.dto.RefreshTokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resetAccessToken")
public class RefreshTokenController {
    @Autowired
    private RefreshTokenService refreshTokenService;
    @PostMapping()
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        return refreshTokenService.refreshAccessToken(request);
    }

}
