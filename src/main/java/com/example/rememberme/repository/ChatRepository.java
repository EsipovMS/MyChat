package com.example.rememberme.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ChatRepository {
    private final Map<Long, Boolean> personId2isTyping = new HashMap<>();

    public void setTyping(Long id, Boolean isTyping) {
        personId2isTyping.put(id, isTyping);
    }

    public Boolean isTyping(Long id) {
        return personId2isTyping.get(id);
    }
}
