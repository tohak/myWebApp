package com.konovalov.myWebApp.repository;

import com.konovalov.myWebApp.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository <Message, Long> {
    //метод поиска в бд всех по тегу
    List<Message> findByTag (String tag);

    Message findById(int id);

}