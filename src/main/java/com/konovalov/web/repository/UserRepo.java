package com.konovalov.web.repository;

import com.konovalov.web.domain.User;
import com.konovalov.web.repository.common.BaseRepository;

public interface UserRepo extends BaseRepository<User, Long> {
    User findByUsername(String username);
    User findByActivationCode(String code);
    User findByEmail(String email);
}
