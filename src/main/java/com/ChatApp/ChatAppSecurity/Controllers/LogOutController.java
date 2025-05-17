package com.ChatApp.ChatAppSecurity.Controllers;


import com.ChatApp.ChatAppSecurity.Service.LogOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogOutController {

    @Autowired
    private LogOutService logoutService;
    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        return logoutService.logout(authentication);
    }
}