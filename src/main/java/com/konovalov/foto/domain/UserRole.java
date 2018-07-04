package com.konovalov.foto.domain;

import org.springframework.security.core.GrantedAuthority;

public enum  UserRole implements GrantedAuthority {
    USER, ADMIN;

    //строковое обозночение роли
    @Override
    public String getAuthority() {
        return name();
    }
}
