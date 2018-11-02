package com.konovalov.myWebApp.service;


import com.konovalov.myWebApp.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageService {
    private final
    MessageRepo messageRepo;

    @Autowired
    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public void deleteMessages(int[] idList) {
        for (int id : idList) {
            messageRepo.deleteById((long) id);
        }
    }
}
