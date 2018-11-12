package com.konovalov.web.controller;


import com.konovalov.web.domain.User;
import com.konovalov.web.domain.dto.CaptchaResponseDto;
import com.konovalov.web.errors.ControllerErrorHandling;
import com.konovalov.web.service.UserService;
import com.konovalov.web.utils.Constants;
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
import java.util.Objects;

@Controller
public class RegistrationController {
    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    private final UserService userService;


    @Value("${recaptcha.secret}")
    private String secret;

    private final RestTemplate restTemplate;

    public RegistrationController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    // get  запросы
    @GetMapping("/registration")
    public String registration() {
        return Constants.REGISTRATION;
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
        if (!Objects.requireNonNull(response).isSuccess()) {
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
        if (userService.checkEmailUsr(user)) {
            model.addAttribute("emailError", "such mail already exists");
        }
        //вывод ошибки если есть
        if (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess() || userService.checkEmailUsr(user)) {
            Map<String, String> errors = ControllerErrorHandling.getErrors(bindingResult);
            model.mergeAttributes(errors);          //merge  потому что мап
            return Constants.REGISTRATION;
        }
        //если такой есть сообщаем на странице регистрации
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists or wrong user name!");
            return Constants.REGISTRATION;
        }
        // вывод сообщения об успешной регистрации
        model.addAttribute(Constants.MESSAGE,
                String.format("Thank you for registering, activation code sent to your Email: %s!", user.getEmail()));

        return "login";
    }

    //сообщаем об активации
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute(Constants.MESSAGE, "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute(Constants.MESSAGE, "Activation code is not found!");
        }
        return "login";
    }
}