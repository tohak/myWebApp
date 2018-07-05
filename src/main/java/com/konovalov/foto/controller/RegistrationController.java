package com.konovalov.foto.controller;


import com.konovalov.foto.domain.User;
import com.konovalov.foto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    // get  запросы
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    // пост запрос по имени юзера,
    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {

        //если такой есть сообщаем на странице регистрации
        if (!userService.addUser(user)) {
            model.put("message", "User exists or wrong user name!");
            return "registration";
        }

        return "redirect:/login";
    }
}