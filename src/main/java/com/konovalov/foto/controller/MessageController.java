package com.konovalov.foto.controller;



import com.konovalov.foto.repository.MessageRepo;
import com.konovalov.foto.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


// удаление сообщений
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
