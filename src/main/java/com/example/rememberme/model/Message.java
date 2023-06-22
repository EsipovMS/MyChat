package com.example.rememberme.model;

import com.example.rememberme.api.MessageStatus;
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
    private MessageStatus status;
    private Long imageId;
    private Long answeredMessageId;
}
