package com.konovalov.web.domain;

import org.springframework.security.core.GrantedAuthority;

public enum  UserRole implements GrantedAuthority {
    USER, ADMIN, ANONYMOUS;

    //строковое обозночение роли
    @Override
    public String getAuthority() {
        return name();
    }
}
