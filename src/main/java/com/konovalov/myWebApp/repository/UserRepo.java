package com.konovalov.myWebApp.repository;

import com.konovalov.myWebApp.domain.User;
import com.konovalov.myWebApp.repository.common.BaseRepository;

public interface UserRepo extends BaseRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);

    User findByEmail(String email);
}
