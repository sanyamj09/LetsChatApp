package com.ChatApp.ChatAppSecurity.Service;

import com.ChatApp.ChatAppSecurity.Repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserSearchService {

    @Autowired
    private UserDataRepository userDataRepository;

    public ResponseEntity<?> searchUser(String username){

        return ResponseEntity.ofNullable(null);
    }
}

