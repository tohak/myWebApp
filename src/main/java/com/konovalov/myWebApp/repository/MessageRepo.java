package com.konovalov.myWebApp.repository;

import com.konovalov.myWebApp.domain.Message;
import com.konovalov.myWebApp.repository.common.BaseRepository;

import java.util.List;

public interface MessageRepo extends BaseRepository<Message, Long> {
    List<Message> findByTag(String tag);

    //Optional<Message> findById(Long id);
}