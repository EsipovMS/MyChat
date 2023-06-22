package com.example.rememberme.shedules;

import com.example.rememberme.model.Person;
import com.example.rememberme.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Async
@RequiredArgsConstructor
public class OnlineSheduler {

    private final PersonRepository personRepository;


    @Scheduled(fixedDelay = 3_000)
    public void setOfflineStatus() {
        List<Person> personList = personRepository.findUsersOnline();
        for (Person person : personList) {
            if (System.currentTimeMillis() - person.getLastOnlineStatusTime().getTime() <= 1000 * 3) continue;
            person.setOnlineStatus(false);
            person.setLastOnlineStatusTime(null);
        }
    }
}
