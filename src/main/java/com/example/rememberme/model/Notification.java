package com.example.rememberme.model;

import lombok.Data;

@Data
public class Notification {
    private Long id;
    private String message;
    private String tag;
    private Long personId;
}
