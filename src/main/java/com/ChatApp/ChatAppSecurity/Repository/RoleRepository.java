package com.ChatApp.ChatAppSecurity.Repository;

import com.ChatApp.ChatAppSecurity.dto.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role , Long> {
    Optional<Role> findByAuthority(String authority);

    boolean existsByAuthority(String authority);
}
