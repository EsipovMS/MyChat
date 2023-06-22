package com.example.rememberme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private Long id;
    private String name;
    private String firstName;
    private String login;
    private String password;
    private String authToken;
    private Boolean onlineStatus;
    private Timestamp lastOnlineStatusTime;
}
