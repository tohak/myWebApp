package com.konovalov.foto.service;

import com.konovalov.foto.domain.Message;
import com.konovalov.foto.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class MessageService {
    @Autowired
    MessageRepo messageRepo;
        public void deleteMessages(Long[] idList) {
        for (long id : idList) {
            System.out.printf("messages aaaa "+ id);
            messageRepo.delete(messageRepo.findById(id));
        }

    }
}
