package com.konovalov.web.controller;

import com.konovalov.web.domain.Message;
import com.konovalov.web.domain.User;
import com.konovalov.web.errors.ControllerErrorHandling;
import com.konovalov.web.repository.MessageRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Controller
public class MainController {
    private final MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public MainController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    //выводим все сообщения с бд
    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        messageRepo.findAll();
        Iterable<Message> messages;
        //filter  поиск по тегу
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    //берем с вью текс и тег и сохраняем его в базу, после выводим на страницу
    @PreAuthorize("hasAuthority('ADMIN') or ('USER')") // даем доступ
    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,    // получение пользователя
            @Valid
                    Message message,
            BindingResult bindingResult,     // для того что бы обрабатывать валиацию, обязательно перед моделью прописывать
            Model model,
            @RequestParam("file") MultipartFile file) throws IOException {
        message.setAuthor(user);
        // если есть ошибки  обрабатывать и вывод во вью, если нет ошибок делать запись в бд
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerErrorHandling.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            // проверяем файл тот что приходит если его нет или пустое имя пропускаем.
            if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
                // создаем дерикторию куда сохранять если ее нету
                File unloadDir = new File(uploadPath);
                if (!unloadDir.exists()) {
                    boolean createFile = unloadDir.mkdir();
                    if (!createFile) {
                        log.error("No access create file");
                    }
                }
                // добовляем к названию файла рандом символы, что бы названия файлов не повторялось
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                // сохраняем файл на сервере
                file.transferTo(new File(uploadPath + File.separator + resultFilename));
                message.setFilename(resultFilename);
            }
            model.addAttribute("message", null);
            messageRepo.save(message);
        }
        //выводим на вью
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }
}
