package com.konovalov.myWebApp.controller;


import com.konovalov.myWebApp.domain.User;
import com.konovalov.myWebApp.domain.UserRole;
import com.konovalov.myWebApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
// контроллер пользователей
@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')") // обозначить прова только админам(по всем метадам, если  нужно отдельно что то то обозначать метод)
public class UserController {
    @Autowired
    private UserRepo userRepo;
    //  выводить лист пользователей
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());

        return "userList";
    }
    // редактор пользователей  вывод вользователей и ролей
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());

        return "userEdit";
    }
    //  получение новых параметров редактирования  и сохранение пользоватя
    @PostMapping
    public String userSave(
            @RequestParam String username,    //  получаение нового имени пользователя
            @RequestParam Map<String, String> form,   // получение отмеченых галочкой ролей
            @RequestParam("userId") User user     //получение юзера по ади
    ) {
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

        return "redirect:/user";
    }
}