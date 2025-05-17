package com.ChatApp.ChatAppSecurity.Service;

import com.ChatApp.ChatAppSecurity.Repository.UserDataRepository;
import com.ChatApp.ChatAppSecurity.dto.UserData;
import com.ChatApp.ChatAppSecurity.dto.UserProfile;
import com.ChatApp.ChatAppSecurity.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {
    @Autowired
    private UserDataRepository userDataRepository;
    @Autowired
    private JwtUtils jwtUtils;

    public ResponseEntity<UserProfile> getUserProfileFromUserName(String username){
        Optional<UserData> userData = userDataRepository.findByUsername(username);
        if(userData.isPresent()){
            UserData userData1 = userData.get();
            UserProfile profile = new UserProfile();
            profile.setBio(userData1.getUserBio());
            profile.setEmail(userData1.getEmail());
            profile.setUsername(userData1.getUsername());
            return new ResponseEntity<>(profile,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
//    public ResponseEntity<?> setUserProfile(UserProfile userProfile){
//        String username = userProfile.getUsername();
//        Optional<UserData> userData = userDataRepository.findByUsername(username);
//        if(userData.isPresent()){
//            UserData userData1 = userData.get();
//            userData1.setUserBio(userProfile.getBio());
//            return new ResponseEntity<>("Updated Successfully",HttpStatus.ACCEPTED);
//        }
//        return new ResponseEntity<>("User Not Found",HttpStatus.BAD_REQUEST);
//
//
//    }

    public ResponseEntity<?> setUserProfile(UserProfile profile ,HttpServletRequest request){
        String token = jwtUtils.getJwtTokenFromHeader(request);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Optional<UserData> userData = userDataRepository.findByUsername(username);
        if(userData.isPresent()){
            UserData userData1 = userData.get();
            userData1.setUserBio(profile.getBio());
            userDataRepository.save(userData1);
            return new ResponseEntity<>("Updated Successfully",HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("User Not Found",HttpStatus.BAD_REQUEST);


    }
}
