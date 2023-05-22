package com.example.rememberme.repository;

import com.example.rememberme.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepository {

    List<Person> personList = List.of(
            new Person(1L,"Михаил", "Есипов", "esipov", "123", null),
            new Person(2L,"Альбина", "Булатова", "bulatova", "123", null)
            );

    public Person findByUsername(String username) {

        for (Person person : personList) {
            if (person.getLogin().equals(username)) return person;
        }
        return null;
    }

    public List<Person> findAll() {
        return personList;
    }

    public Person findByLogin(String email) {
        for (Person person : personList) {
            if (person.getLogin().equals(email)) return person;
        }
        return null;
    }

    public void setTokenById(Long id, String jwt) {
        for (Person person : personList) {
            if (person.getId().equals(id)) person.setAuthToken(jwt);
        }
    }

    public Person findByToken(String token) {
        for (Person person : personList) {
            if (person.getAuthToken().equals(token)) return person;
        }
        return null;
    }
}
