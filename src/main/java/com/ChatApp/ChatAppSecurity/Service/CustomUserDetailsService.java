package com.ChatApp.ChatAppSecurity.Service;

import com.ChatApp.ChatAppSecurity.Repository.UserDataRepository;
import com.ChatApp.ChatAppSecurity.dto.CustomUserDetails;
import com.ChatApp.ChatAppSecurity.dto.UserData;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDataRepository userDataRepository;

    public CustomUserDetailsService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user from the database
        UserData user = userDataRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new CustomUserDetails(user);
    }
}