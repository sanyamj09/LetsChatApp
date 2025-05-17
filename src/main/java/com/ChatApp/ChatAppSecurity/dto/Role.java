package com.ChatApp.ChatAppSecurity.dto;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @ManyToMany(mappedBy = "roles")
    private Collection<UserData> users;
    public Role(String authority) {
        this.authority = authority;
    }

    public Role() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String authority; // this is required by GrantedAuthority

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
