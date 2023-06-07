package com.example.rememberme.controller;

import com.example.rememberme.model.ChatDetails;
import com.example.rememberme.service.ChatDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatDetailsService chatDetailsService;
    @GetMapping("/chatDetails")
    public ChatDetails getChatDetails() {
        ChatDetails chatDetails = chatDetailsService.getDetails();
        return chatDetails;
    }
    @PostMapping("/typing")
    public Boolean isTyping(@RequestParam Boolean isTyping) {
        return chatDetailsService.setTyping(isTyping);
    }

    @GetMapping("/typing")
    public Boolean isDstPersonTyping() {
        return chatDetailsService.isDstPersonTyping();
    }
}
