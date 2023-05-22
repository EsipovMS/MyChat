package com.example.rememberme.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Message {
    private Long id;
    private String messageText;
    private Long authorId;
    private Timestamp time;
    private Boolean isRead;
    private Boolean isDeleted;
    private Boolean isDelivered;
}
