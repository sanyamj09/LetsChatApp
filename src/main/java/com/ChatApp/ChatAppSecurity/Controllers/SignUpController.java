package com.ChatApp.ChatAppSecurity.Controllers;
import com.ChatApp.ChatAppSecurity.Service.SignUpService;
import com.ChatApp.ChatAppSecurity.dto.SignUpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class SignUpController {
    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);
    @Autowired
    private SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest){
        logger.debug("Registering User");
        if(signUpService.registerUser(signUpRequest)){
            return new ResponseEntity<>("User Registered Successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("User Already Exist",HttpStatus.ALREADY_REPORTED);
    }
}




