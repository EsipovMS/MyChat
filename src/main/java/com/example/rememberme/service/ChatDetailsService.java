package com.example.rememberme.service;

import com.example.rememberme.model.ChatDetails;
import com.example.rememberme.model.Person;
import com.example.rememberme.repository.ChatRepository;
import com.example.rememberme.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatDetailsService {

    private final PersonRepository personRepository;

    private final ChatRepository chatRepository;

    private final AuthService authService;

    public ChatDetails getDetails() {
        Person me = personRepository.findByUsername(authService.getCurrentUsername());
        Person person = personRepository.findByUsername(authService.getCurrentUsername());
        List<Person> personList = personRepository.findAll();
        for (Person p : personList) {
            if (!p.equals(person)) return new ChatDetails(p, me);
        }
        return null;
    }

    public Boolean setTyping(Boolean isTyping) {
        Person me = personRepository.findByUsername(authService.getCurrentUsername());
        chatRepository.setTyping(me.getId(), isTyping);
        return chatRepository.isTyping(me.getId());
    }

    public Boolean isDstPersonTyping() {
        return chatRepository.isTyping(getDetails().getDstPerson().getId());
    }
}
