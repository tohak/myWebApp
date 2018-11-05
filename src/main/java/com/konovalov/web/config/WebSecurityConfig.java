package com.konovalov.web.config;

import com.konovalov.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // включить глобальный поиск прав доступа по проєкту
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    //куда какой доступ по страницам
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()   // без авторизации нечего нельзя делать
                .antMatchers("/", "/h2-console/**", "/registration", "/reactivateemail", "/user/passwordreset", "/static/**", "/static/images/**", "/activate/**").permitAll() // перейти на эту страницу может только пользователи групп(все)
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**")   // добавить в защите игнор консоле
                .and()
                .rememberMe()
                .and()
                .logout()
                .permitAll()
                .and()
                .headers().frameOptions().sameOrigin();// разрешить использовать frame с одинаковыми url
    }

    //ходить по бд  табличка юзер и их роли
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService) //шифруем пароль
                .passwordEncoder(passwordEncoder);
    }

}
