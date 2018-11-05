package com.konovalov.web.repository;

import com.konovalov.web.domain.Message;
import com.konovalov.web.repository.common.BaseRepository;

import java.util.List;

public interface MessageRepo extends BaseRepository<Message, Long> {
    List<Message> findByTag(String tag);
}