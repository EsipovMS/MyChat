package com.example.rememberme.repository;

import com.example.rememberme.api.MessageStatus;
import com.example.rememberme.model.Message;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MessageRepository {

    private List<Message> messages = new ArrayList<>();

    public void testDump() {
        for (int i = 0; i < 100; i++) {
            Message message = new Message();
            message.setId((long) i);
            message.setMessageText("some message test text " + i);
            message.setIsRead(true);
            message.setIsDelivered(true);
            message.setIsDeleted(false);
            long authorId;
            if (i % 3 == 0) {
                authorId = 2L;
            } else {
                authorId = 1L;
            }
            message.setAuthorId(authorId);
            message.setTime(new Timestamp(System.currentTimeMillis() - 150_000L - i * 1000));
            messages.add(message);
        }
    }

    public List<Message> findAll() {
        return messages;
    }

    public void save(Message message) {
        messages.add(message);
    }

    public Message findById(Long id) {
        for (Message m : messages) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    public void delete(Message message) {
        messages.remove(message);
    }

    public void updateAllById(List<Long> messagesToRead) {
        for (Message message : messages) {
            if (!messagesToRead.contains(message.getId())) continue;
            message.setStatus(MessageStatus.READ);
        }
    }
}
