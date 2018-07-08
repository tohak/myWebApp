package com.konovalov.foto.controller;


import com.konovalov.foto.domain.Message;
import com.konovalov.foto.repository.MessageRepo;
import com.konovalov.foto.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



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
            @RequestParam("toDelete[]") Long[] toDelete){
        for (long id: toDelete){
            System.out.printf("message aaa "+id);
            Message messageY=messageRepo.findById(id);
            System.out.println(messageY);
        }

//        if (toDelete != null && toDelete.length > 0){
//            for (long id: toDelete) {
//                System.out.printf("message aaaa"+ id);
//                messageRepo.delete(messageRepo.findById(id));
//            }
//        }

        return "redirect:/main";
    }
}
