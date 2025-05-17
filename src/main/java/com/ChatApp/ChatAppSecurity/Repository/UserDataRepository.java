package com.ChatApp.ChatAppSecurity.Repository;

import com.ChatApp.ChatAppSecurity.dto.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {

    Optional<UserData> findByUsername(String username);
    Optional<UserData> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}
