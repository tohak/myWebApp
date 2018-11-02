package com.konovalov.myWebApp.domain;


import com.konovalov.myWebApp.domain.common.BaseEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder
@Entity
@Table(name = "usr_tbl")
public class User extends BaseEntity implements UserDetails {
    public static final boolean BUN_NULL = false;
    public static final int LENGTH = 100;

    @NotBlank(message = "Name is required")// не занесет в базу пустое поле
    @Column(name = "user_name", length = LENGTH, nullable = BUN_NULL)
    private String username;

    @NotBlank(message = "Password cannot empty")
    @Column(name = "password", length = LENGTH, nullable = BUN_NULL)
    private String password;

    @Column(name = "active")
    private boolean active;

    @NotBlank(message = "Name is required")
    @Email(message = "Email is not correct")
    @Column(name = "email", nullable = BUN_NULL, length = LENGTH, unique = true)
    private String email;
    @Column(name = "activate_code")
    private String activationCode;

    /**
     * Тут можно переделать что бы связь была много ко многим, но пока не хочу рушить логику.
     * Если нада  сдлеать скажите переделаю
     */
    // создание еще 1 таблици  роли связаной с юзером
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    // указать какой класс и подгрузка две таблици или по необходимости
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    // название таблици и связаное поле
    @Enumerated(EnumType.STRING)  //  хранение в текстовом
    private Set<UserRole> roles;


    public User(@NotBlank(message = "Name is required") String username, @NotBlank(message = "Name is required") String password, boolean active, String email, Set<UserRole> roles) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.email = email;
        this.roles = roles;
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

    // проверка с бд берет данные узнает админ или нет
    public boolean isAdmin() {
        return roles.contains(UserRole.ADMIN);
    }

    public boolean isUserAut() {
        return roles.contains(UserRole.USER);
    }

    public boolean isAnonymous() {
        return roles.contains(UserRole.ANONYMOUS);
    }


}
