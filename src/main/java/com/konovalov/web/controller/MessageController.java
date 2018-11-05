package com.konovalov.web.controller;


import com.konovalov.web.repository.MessageRepo;
import com.konovalov.web.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


// удаление сообщений
@PreAuthorize("hasAuthority('ADMIN')") // даем доступ только админам
@Controller
public class MessageController {
    private final MessageRepo messageRepo;
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageRepo messageRepo, MessageService messageService) {
        this.messageRepo = messageRepo;
        this.messageService = messageService;
    }

    @GetMapping("/messagesList")
    public String messagesList(Model model) {
        model.addAttribute("messages", messageRepo.findAll());
        return "messagesList";
    }

    @PostMapping("messagesList")
    public String messagesdelete(
            @RequestParam("toDelete[]") int[] toDelete) {
        messageService.deleteMessages(toDelete);
        return "redirect:/main";
    }
}
