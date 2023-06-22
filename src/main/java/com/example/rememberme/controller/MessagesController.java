package com.example.rememberme.controller;

import com.example.rememberme.api.MessageRs;
import com.example.rememberme.service.MessagesService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
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
    public MessageRs sendMessage(@RequestParam String textMessage,
                                 @RequestParam String answeredMessageId,
                                 @RequestParam(required = false) MultipartFile image) throws TelegramApiException, IOException {
        return messagesService.sendMessages(textMessage, image, answeredMessageId);
    }

    @DeleteMapping("/messages/{id}")
    public MessageRs deleteMessage(@PathVariable Long id) {
        return messagesService.deleteMessage(id);
    }

    @PostMapping("/readMessages")
    public List<MessageRs> readMessages(@RequestParam String messagesToRead) throws org.json.simple.parser.ParseException {
        return messagesService.readMessages(messagesToRead);
    }
}
