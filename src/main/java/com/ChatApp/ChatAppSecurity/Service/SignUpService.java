package com.ChatApp.ChatAppSecurity.Service;

import com.ChatApp.ChatAppSecurity.Repository.RoleRepository;
import com.ChatApp.ChatAppSecurity.Repository.UserDataRepository;
import com.ChatApp.ChatAppSecurity.dto.Role;
import com.ChatApp.ChatAppSecurity.dto.SignUpRequest;
import com.ChatApp.ChatAppSecurity.dto.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignUpService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDataRepository userDataRepository;
    @Autowired
    private RoleRepository roleRepository;

    public boolean registerUser(SignUpRequest signUpRequest){
        if(userDataRepository.existsByUsername(signUpRequest.getUsername())){return false;}
        UserData user = new UserData();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setEnabled(true); // important for Spring Security
        Role userRole = roleRepository.findByAuthority("ROLE_USER").orElseThrow(()->new RuntimeException("Role not Found"));
        user.setRoles(List.of(userRole));

        // Lambda as GrantedAuthority

        userDataRepository.save(user);
        return true;
    }
}
