package com.konovalov.web.service;


import com.konovalov.web.repository.MessageRepo;
import org.springframework.stereotype.Service;


@Service
public class MessageService {
    private final
    MessageRepo messageRepo;


    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public void deleteMessages(int[] idList) {
        for (int id : idList) {
            messageRepo.deleteById((long) id);
        }
    }
}
