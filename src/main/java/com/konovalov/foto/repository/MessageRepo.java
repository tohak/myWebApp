package com.konovalov.foto.repository;

import com.konovalov.foto.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepo extends CrudRepository <Message, Long> {
    //метод поиска в бд всех по тегу
    List<Message> findByTag (String tag);
    Message findById(long id);

    void removeById(long id);
}