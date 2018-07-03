package com.konovalov.foto.controller;


import com.konovalov.foto.domain.User;
import com.konovalov.foto.domain.UserRole;
import com.konovalov.foto.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    // get  запросы
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    // пост запрос по имени юзера, если такой есть сообщаем на странице регистрации
    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(UserRole.USER));
        userRepo.save(user);

        return "redirect:/login";
    }
}