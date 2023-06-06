package com.example.rememberme.service;

import com.example.rememberme.config.BotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class BotService extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    @Value("${bot.enable}")
    private boolean enable;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().getText().equals("/start")) {
            String messageText = "Привет! Здесь будут уведомления из чата";
            System.out.println(update.getMessage().getChatId());
            try {
                sendMessage(update.getMessage().getChatId(), messageText);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(Long chatId, String messageText) throws TelegramApiException {
        if (!enable) return;
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText);
        execute(message);
    }
}
