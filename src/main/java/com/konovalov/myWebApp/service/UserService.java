package com.konovalov.myWebApp.service;


import com.konovalov.myWebApp.domain.User;
import com.konovalov.myWebApp.domain.UserRole;
import com.konovalov.myWebApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailSender mailSender;
    @Value("${url.mail.activation}")
    private String stringUrl;

    //метод возрашение пользователя по имени
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
        user.setRoles(Collections.singleton(UserRole.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);

        sendMessage(user);

        return true;
    }
//отправка активации почты
    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to My Web App. Please, visit next link:" + stringUrl + "%s",
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

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);

            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        if (!StringUtils.isEmpty(password)){
            user.setPassword(password);
        }
        userRepo.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }
    }
}
