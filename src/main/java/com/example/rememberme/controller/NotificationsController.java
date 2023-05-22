package com.example.rememberme.controller;

import com.example.rememberme.model.Notification;
import com.example.rememberme.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationsController {

    private final NotificationService notificationService;

    @GetMapping("/notification")
    public Notification getNotification() {
        return notificationService.getNotification();
    }
}
