package com.ChatApp.ChatAppSecurity.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class UserSearchController {
    @GetMapping("/{username}")
    public ResponseEntity<?> searchUserByUsername(@PathVariable("username") String username ){
        return new ResponseEntity<>("user", HttpStatus.OK);
    }
}
