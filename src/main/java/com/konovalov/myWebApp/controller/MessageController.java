package com.konovalov.myWebApp.controller;



import com.konovalov.myWebApp.repository.MessageRepo;
import com.konovalov.myWebApp.service.MessageService;
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
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private MessageService messageService;

    @GetMapping("/messagesList")
    public String messagesList (Model model){
        model.addAttribute("messages", messageRepo.findAll());
        return "messagesList";
    }
    @PostMapping("messagesList")
    public String messagesdelete(
            @RequestParam("toDelete[]") int[] toDelete){
       messageService.deleteMessages(toDelete);

        return "redirect:/main";
    }
}
