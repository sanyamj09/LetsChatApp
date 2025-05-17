package com.ChatApp.ChatAppSecurity.Controllers;


import com.ChatApp.ChatAppSecurity.Service.SignInService;
import com.ChatApp.ChatAppSecurity.dto.SignInRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/signin")
public class SignInController {
    @Autowired
    private SignInService signInService;

    @PostMapping()
    public ResponseEntity<?> signInUser(@RequestBody SignInRequest signInRequest){
        return signInService.validateUser(signInRequest);
    }
}

