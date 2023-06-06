package com.example.rememberme.service;

import com.example.rememberme.api.MessageRs;
import com.example.rememberme.api.MessageStatus;
import com.example.rememberme.mappers.MessagesMapper;
import com.example.rememberme.model.Message;
import com.example.rememberme.model.Notification;
import com.example.rememberme.model.Person;
import com.example.rememberme.repository.MessageRepository;
import com.example.rememberme.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessagesService {

    private final MessageRepository messageRepository;
    private final AuthService authService;
    private final PersonRepository personRepository;
    private final NotificationService notificationService;
    private final BotService botService;

    private long counter = 0;

    public MessageRs sendMessages(String textMessage) throws TelegramApiException {
        Person author = personRepository.findByUsername(authService.getCurrentUsername());
        Message message = new Message();
        message.setId(counter++);
        message.setMessageText(textMessage);
        message.setIsDeleted(false);
        message.setIsDelivered(false);
        message.setIsRead(false);
        message.setTime(new Timestamp(System.currentTimeMillis()));
        message.setAuthorId(author.getId());
        message.setStatus(MessageStatus.SEND);
        messageRepository.save(message);
        MessageRs messageRs = MessagesMapper.INSTANCE.toDTO(message, true);
        sendNotification(message);
        return messageRs;
    }

    private void sendNotification(Message message) throws TelegramApiException {
        Notification notification = new Notification();
        notification.setId(counter++);
        notification.setMessage("Новое сообщение");
        notification.setTag("Сообщение");
        long id;
        if (message.getAuthorId() == 1L) {
            id = 2L;
            botService.sendMessage(2086960389L, message.getMessageText());
        } else {
            id = 1L;
            botService.sendMessage(1004065640L, message.getMessageText());
        }
        notification.setPersonId(id);
        notificationService.setNotification(notification);
    }

    public synchronized List<MessageRs> getMessages() {
        Person me = personRepository.findByUsername(authService.getCurrentUsername());
        List<Message> messages = messageRepository.findAll();
        messages.sort(Comparator.comparing(Message::getTime));
        List<MessageRs> messageRsList = new ArrayList<>();
        for (Message message : messages) {
            boolean income;
            income = message.getAuthorId() == me.getId();
            MessageRs messageRs = MessagesMapper.INSTANCE.toDTO(message, income);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyy hh:mm");
            messageRs.setTime(formatter.format(message.getTime().toLocalDateTime()));
            messageRsList.add(messageRs);
        }
        return messageRsList;
    }

    public void test() {
        messageRepository.testDump();
    }

    public MessageRs deleteMessage(Long id) {
        Message message = messageRepository.findById(id);
        messageRepository.delete(message);
        boolean income = false;
        if (message.getAuthorId() == 1L) income = false;
        return MessagesMapper.INSTANCE.toDTO(message, income);
    }

    public List<MessageRs> readMessages(String messagesToRead) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(messagesToRead);
        JSONArray messagesToReadArr = (JSONArray)obj;
        Person me = personRepository.findByUsername(authService.getCurrentUsername());
        List<Long> messagesToReadIds = new ArrayList<>();
        for (Object o : messagesToReadArr) {
            Long id = Long.parseLong(o.toString());
            messagesToReadIds.add(id);
        }
        messageRepository.updateAllById(messagesToReadIds);
        List<MessageRs> messageRsList = new ArrayList<>();
        for (Long aLong : messagesToReadIds) {
            Message message = messageRepository.findById(aLong);
            boolean income;
            income = message.getAuthorId() == me.getId();
            MessageRs messageRs = MessagesMapper.INSTANCE.toDTO(message, income);
            messageRsList.add(messageRs);
        }
        return messageRsList;
    }
}
