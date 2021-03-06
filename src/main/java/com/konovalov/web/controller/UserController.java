package com.konovalov.web.controller;


import com.konovalov.web.domain.User;
import com.konovalov.web.domain.UserRole;
import com.konovalov.web.service.UserService;
import com.konovalov.web.utils.Constants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// контроллер пользователей
@Controller
@RequestMapping("/user")

public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    //  выводить лист пользователей
    @PreAuthorize("hasAuthority('ADMIN')") // обозначить прова только админам
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    // редактор пользователей  вывод вользователей и ролей
    @PreAuthorize("hasAuthority('ADMIN')") // обозначить прова только админам
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());

        return "userEdit";
    }

    //  получение новых параметров редактирования  и сохранение пользоватя
    @PreAuthorize("hasAuthority('ADMIN')") // обозначить прова только админам
    @PostMapping
    public String userSave(
            @RequestParam String username,    //  получаение нового имени пользователя
            @RequestParam Map<String, String> form,   // получение отмеченых галочкой ролей
            @RequestParam("userId") User user //получение юзера по ади
    ) {
        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    // изменение профайла пользователем
    @GetMapping("profile")
    public String getProfile(Model model,
                             @AuthenticationPrincipal User user) {    // получение пользователя из сессии( берет авторизировоного пользователя)
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        if (userService.isUserRole(user, UserRole.ANONYMOUS)) {
            model.addAttribute(Constants.MESSAGE, "To work on the site you need to activate mail");
        }
        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String passwordold,
            @RequestParam String password,
            @RequestParam String password2,
            @RequestParam String email) {
        userService.updateProfile(user, passwordold, password, password2, email);
        return "redirect:/user/profile";
    }

    //  сброс пароля на почту
    @GetMapping("passwordreset")
    public String passwordreset() {
        return "passwordreset";
    }

    @PostMapping("passwordreset")
    public String updatePasswordReset(@RequestParam String username,
                                      @RequestParam String email,
                                      Model model) {
        boolean isRefreshPassword = userService.refreshPassword(username, email);
        if (isRefreshPassword) {
            model.addAttribute(Constants.MESSAGE, "New password sent to e-mail");
        } else {
            model.addAttribute(Constants.MESSAGE, "User or mail entered incorrectly");
        }
        return "passwordreset";
    }

    //  повторная активация почты
    @GetMapping("reactivateemail")
    public String setReactivateemail(Model model,
                                     @AuthenticationPrincipal User user) {    // получение пользователя из сессии( берет авторизировоного пользователя)
        model.addAttribute("username", user.getUsername());
        userService.sendMessage(user);
        model.addAttribute(Constants.MESSAGE, "Activation code sent to your e-mail");

        return "reactivateemail";
    }

}