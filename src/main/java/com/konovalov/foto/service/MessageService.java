package com.konovalov.foto.service;


import com.konovalov.foto.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class MessageService {
    @Autowired
    MessageRepo messageRepo;
        public void deleteMessages(int[] idList) {
        for (int id : idList) {
            messageRepo.delete(messageRepo.findById(id));
        }

    }
}
