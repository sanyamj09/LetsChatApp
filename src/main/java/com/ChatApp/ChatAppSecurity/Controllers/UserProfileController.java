package com.ChatApp.ChatAppSecurity.Controllers;

import com.ChatApp.ChatAppSecurity.Service.UserProfileService;
import com.ChatApp.ChatAppSecurity.dto.UserProfile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userprofile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;
    @GetMapping("/{username}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable("username") String username){
            return userProfileService.getUserProfileFromUserName(username);
    }
    @PutMapping()
    public ResponseEntity<?> updateUserProfile(@RequestBody UserProfile userProfile, HttpServletRequest request){
        return userProfileService.setUserProfile(userProfile,request);
    }

}
