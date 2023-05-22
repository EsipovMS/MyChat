package com.example.rememberme.service;

import com.example.rememberme.model.ChatDetails;
import com.example.rememberme.model.Person;
import com.example.rememberme.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatDetailsService {

    private final PersonRepository personRepository;

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
}
