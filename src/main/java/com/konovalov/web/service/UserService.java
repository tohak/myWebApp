package com.konovalov.web.service;


import com.konovalov.web.domain.User;
import com.konovalov.web.domain.UserRole;
import com.konovalov.web.repository.UserRepo;
import com.konovalov.web.utils.RandomPassword;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@PropertySource("classpath:mail.properties")
@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Value("${url.mail.activation}")
    private String stringUrl;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }
    //метод возрашение пользователя по имени
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    // добавление пользователя
    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        // если в базе уже есть такое имя или введенное поле имени пусто то возращаем фолсе
        if (userFromDb != null || user.getUsername().isEmpty()) {
            return false;
        }
        // если нет сохраняем в базу
        user.setActive(true);
        user.setRoles(Collections.singleton(UserRole.ANONYMOUS));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);
        sendMessage(user);
        return true;
    }
    // проверка на сущестующую почту
    public boolean checkEmailUsr(User user) {
        User userBd = userRepo.findByEmail(user.getEmail());
        return userBd == null;
    }
    //отправка активации почты
    public void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to My web App. Please, visit next link:" + stringUrl + "%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }
    //ищем по коду активации пользователя и проверяем есть ли такой пользователь
    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        user.getRoles().clear();
        user.getRoles().add(UserRole.USER);
        userRepo.save(user);
        return true;
    }
    public List<User> findAll() {
        return userRepo.findAll();
    }
    // редактирование пользователя
    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);    // переименоваем пользователя
        // все отмеченые роли переводим в строку и запихиваем в сет
        Set<String> roles = Arrays.stream(UserRole.values())
                .map(UserRole::name)
                .collect(Collectors.toSet());
        //  нужно очистить роли, что бы потом все отмеченые добавить
        user.getRoles().clear();
        // перебераем форму (где есть адишник, токен и роль) вытягиваем только роль и  перезаписываем ее пользователю
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(UserRole.valueOf(key));
            }
        }
        userRepo.save(user);
    }
    // релактирование своего профиля
    public void updateProfile(User user, String passwordold, String password, String password2, String email) {
        String userEmail = user.getEmail();
        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));
        if (isEmailChanged) {
            user.setEmail(email);
            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        boolean isOldPassword = passwordEncoder.matches(passwordold, user.getPassword());// сравнивать  старый введенный пароль и пароль с бд
        if (!StringUtils.isEmpty(password) && password.equals(password2) && isOldPassword) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepo.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }
    }
    // сброс пароля
    public boolean refreshPassword(String userName, String email) {
        String refreshPassword = new RandomPassword().whenGeneratingRandomString();
        User user = userRepo.findByUsername(userName);
        if (user != null && !StringUtils.isEmpty(email) && user.getEmail().equals(email)) {
            String message = String.format(
                    "Hello, %s! \n" +
                            " You new password:  %s",
                    user.getUsername(),
                    refreshPassword);
            mailSender.send(user.getEmail(), "You New password", message);
            user.setPassword(passwordEncoder.encode(refreshPassword));
            userRepo.save(user);
            return true;
        }
        return false;
    }
    //узнать пренадлежит ли юзер этой роли группы
    public boolean isUserRole(User user, UserRole userRole) {
        Set<UserRole> userRoleSet = user.getRoles();
        for (UserRole role : userRoleSet) {
            if (role == userRole) {
                return true;
            }
        }
        return false;
    }
}
