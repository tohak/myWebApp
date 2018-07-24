package com.konovalov.myWebApp.repository;

import com.konovalov.myWebApp.domain.User;
import com.konovalov.myWebApp.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername (String username);

    User findByActivationCode(String code);
    User findByEmail (String email);
}
