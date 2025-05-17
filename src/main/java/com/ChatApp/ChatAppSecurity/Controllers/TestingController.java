package com.ChatApp.ChatAppSecurity.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestingController {

    @GetMapping()
    public ResponseEntity<?> testApi(){
        return new ResponseEntity<>("This APi is working", HttpStatus.OK);
    }
}
