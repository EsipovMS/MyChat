package com.example.rememberme.controller;

import com.example.rememberme.api.MessageRs;
import com.example.rememberme.service.MessagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessagesController {

    private final MessagesService messagesService;

    @GetMapping("/test")
    public String test() {
        messagesService.test();
        return "OK";
    }

    @GetMapping("/messages")
    public List<MessageRs> getMessages() {
        return messagesService.getMessages();
    }

    @PostMapping("/messages")
    public MessageRs sendMessage(@RequestParam String textMessage) throws TelegramApiException {
        return messagesService.sendMessages(textMessage);
    }

    @DeleteMapping("/messages/{id}")
    public MessageRs deleteMessage(@PathVariable Long id) {
        return messagesService.deleteMessage(id);
    }
}