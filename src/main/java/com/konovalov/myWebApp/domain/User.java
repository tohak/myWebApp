package com.konovalov.myWebApp.domain;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name="usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required")// не занесет в базу пустое поле
    private String username;
    @NotBlank(message = "Password cannot empty")
    private String password;
    private boolean active;
    @NotBlank(message = "Name is required")
    @Email(message = "Email is not correct")
    private String email;
    private String activationCode;
// создание еще 1 таблици  роли связаной с юзером
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)  // указать какой класс и подгрузка две таблици или по необходимости
    @CollectionTable(name="user_role", joinColumns = @JoinColumn (name = "user_id"))  // название таблици и связаное поле
    @Enumerated(EnumType.STRING)  //  хранение в текстовом
    private Set<UserRole> roles;

    public User() {
    }

    public User(@NotBlank(message = "Name is required") String username, @NotBlank(message = "Name is required") String password, boolean active, String email, Set<UserRole> roles) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.email = email;
        this.roles = roles;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }
    // проверка с бд берет данные узнает админ или нет
    public boolean isAdmin(){
        return roles.contains(UserRole.ADMIN);
    }
}
