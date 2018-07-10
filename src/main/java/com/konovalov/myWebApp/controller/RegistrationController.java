package com.konovalov.myWebApp.controller;




import com.konovalov.myWebApp.domain.User;
import com.konovalov.myWebApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.validation.Valid;
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
    public String addUser(
            @RequestParam("password2") String passwordConfim,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    )    {
        boolean isConfirmEmpty =StringUtils.isEmpty(passwordConfim);
            // если пароль второй пусто ошибку
        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
        }
        //  проверяем совпадения пароля
        if (user.getPassword()!=null && user.getPassword().equals(passwordConfim)){
            model.addAttribute("passwordError", "password are different!");
        }
        //вывод ошибки если есть
        if (isConfirmEmpty || bindingResult.hasErrors()){
            Map<String, String> errors=ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);          //merge  потому что мап
            return "registration";
        }
        //если такой есть сообщаем на странице регистрации
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists or wrong user name!");
            return "registration";
        }

        return "redirect:/login";
    }
    //сообщаем об активации
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}