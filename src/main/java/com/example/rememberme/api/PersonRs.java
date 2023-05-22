package com.example.rememberme.api;

import lombok.Data;

@Data
public class PersonRs {
    private Long id;
    private String login;
    private String name;
    private String firstName;
    private String token;
    private String expireTime;
    private boolean isOnline;
}
