package com.example.rememberme.service;

import com.example.rememberme.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private Notification notification;

    public Notification getNotification() {
        Notification not = notification;
        notification = null;
        return not;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
