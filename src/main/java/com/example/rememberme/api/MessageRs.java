package com.example.rememberme.api;

import lombok.Data;

@Data
public class MessageRs {
    private Long id;
    private String messageText;
    private String time;
    private Boolean income;
    private Boolean isRead;
    private Boolean isDeleted;
    private Boolean isDelivered;
    private MessageStatus status;
    private Long imageId;
}
