package com.example.rememberme.service;

import com.example.rememberme.api.MessageRs;
import com.example.rememberme.api.MessageStatus;
import com.example.rememberme.mappers.MessagesMapper;
import com.example.rememberme.model.Image;
import com.example.rememberme.model.Message;
import com.example.rememberme.model.Notification;
import com.example.rememberme.model.Person;
import com.example.rememberme.repository.ImagesRepository;
import com.example.rememberme.repository.MessageRepository;
import com.example.rememberme.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessagesService {

    private final MessageRepository messageRepository;
    private final AuthService authService;
    private final PersonRepository personRepository;
    private final NotificationService notificationService;
    private final BotService botService;
    private final ImagesRepository imagesRepository;

    private long counter = 0;
    private long imageCounter = 0;

    public MessageRs sendMessages(String textMessage, MultipartFile image, String answeredMessageId) throws TelegramApiException, IOException {
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
        if (image != null) {
            Long imageId = saveImage(image, message.getId());
            message.setImageId(imageId);
        }
        messageRepository.save(message);
        Message answeredMessage = null;
        if (!Objects.equals(answeredMessageId, "null") && !Objects.equals(answeredMessageId, "undefined")) {
            message.setAnsweredMessageId(Long.parseLong(answeredMessageId));
            answeredMessage = messageRepository.findById(Long.parseLong(answeredMessageId));
        }
        MessageRs messageRs = MessagesMapper.INSTANCE.toDTO(message, true, answeredMessage);
        sendNotification(message);
        return messageRs;
    }

    private Long saveImage(MultipartFile imageFile, Long id) throws IOException {
        Image image = new Image();
        image.setId(imageCounter++);
        image.setName(imageFile.getOriginalFilename());
        image.setSize(imageFile.getSize());
        image.setBytes(imageFile.getBytes());
        imagesRepository.save(image);
        return image.getId();
    }

    private void sendNotification(Message message) throws TelegramApiException {
        Notification notification = new Notification();
        notification.setId(counter++);
        notification.setMessage("Новое сообщение");
        notification.setTag("Сообщение");
        long id;
        if (message.getAuthorId() == 1L) {
            id = 2L;
            botService.sendMessage(2086960389L, "Новое сообщение");
        } else {
            id = 1L;
            botService.sendMessage(1004065640L, "Новое сообщение");
        }
        notification.setPersonId(id);
        notificationService.setNotification(notification);
    }

    public synchronized List<MessageRs> getMessages() {
        Person me = personRepository.findByUsername(authService.getCurrentUsername());
        me.setLastOnlineStatusTime(new Timestamp(System.currentTimeMillis()));
        me.setOnlineStatus(true);
        List<Message> messages = messageRepository.findAll();
        messages.sort(Comparator.comparing(Message::getTime));
        List<MessageRs> messageRsList = new ArrayList<>();
        for (Message message : messages) {
            boolean income;
            income = message.getAuthorId() == me.getId();
            Message answeredMessage = messageRepository.findById(message.getAnsweredMessageId());
            MessageRs messageRs = MessagesMapper.INSTANCE.toDTO(message, income, answeredMessage);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm");
            messageRs.setTime(formatter.format(message.getTime().toLocalDateTime().plusHours(5)));
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
        Message answeredMessage = messageRepository.findById(message.getAnsweredMessageId());
        return MessagesMapper.INSTANCE.toDTO(message, income, answeredMessage);
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
            Message answeredMessage = messageRepository.findById(message.getAnsweredMessageId());
            MessageRs messageRs = MessagesMapper.INSTANCE.toDTO(message, income, answeredMessage);
            messageRsList.add(messageRs);
        }
        return messageRsList;
    }
}
