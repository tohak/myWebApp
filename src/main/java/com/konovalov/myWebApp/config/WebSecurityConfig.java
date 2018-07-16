package com.konovalov.myWebApp.config;

import com.konovalov.myWebApp.service.UserService;
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
@EnableGlobalMethodSecurity (prePostEnabled = true) // включить глобальній поиск прав доступа по проєкту
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

//куда какой доступ по страницам
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .authorizeRequests()   // без авторизации нечего нельзя делать
                    .antMatchers("/", "/registration", "/static/**", "/activate/**", "/activate/static/**", "/user/static/images/*").permitAll() // перейти на эту страницу может только пользователи групп(все)
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .rememberMe()
                .and()
                    .logout()
                    .permitAll();
    }

//ходить по бд  табличка юзер и их роли
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService) //шифруем пароль
                .passwordEncoder(passwordEncoder);
    }

}
