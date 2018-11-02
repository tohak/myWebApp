package com.konovalov.myWebApp.controller;


import com.konovalov.myWebApp.domain.User;
import com.konovalov.myWebApp.domain.dto.CaptchaResponseDto;
import com.konovalov.myWebApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    private final UserService userService;
    @Value("${recaptcha.secret}")
    private String secret;

    private final RestTemplate restTemplate;

    @Autowired
    public RegistrationController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    // get  запросы
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    // пост запрос по имени юзера,
    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfim,
            @RequestParam("g-recaptcha-response") String captchaResponce,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        //каптча
        String url = String.format(CAPTCHA_URL, secret, captchaResponce);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }


        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfim);
        // если пароль второй пусто ошибку
        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
        }
        //  проверяем совпадения пароля
        if (user.getPassword() != null && user.getPassword().equals(passwordConfim)) {
            model.addAttribute("passwordError", "password are different!");
        }
        // проверка на сущестующий имейл
        if (!userService.checkEmailUsr(user)) {
            model.addAttribute("emailError", "such mail already exists");
        }
        //вывод ошибки если есть
        if (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess() || !userService.checkEmailUsr(user)) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);          //merge  потому что мап
            return "registration";
        }
        //если такой есть сообщаем на странице регистрации
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists or wrong user name!");
            return "registration";
        }
        // вывод сообщения об успешной регистрации
        model.addAttribute("message",
                String.format("Thank you for registering, activation code sent to your Email: %s!", user.getEmail()));

        return "login";
    }

    //сообщаем об активации
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }
        return "login";
    }
}