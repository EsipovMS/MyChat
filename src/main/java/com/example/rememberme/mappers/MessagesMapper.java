package com.example.rememberme.mappers;

import com.example.rememberme.api.MessageRs;
import com.example.rememberme.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper()
public interface MessagesMapper {

    MessagesMapper INSTANCE = Mappers.getMapper(MessagesMapper.class);

    List<MessageRs> toDTO(List<Message> messages);

    @Mapping(target = "income", source = "income")
    @Mapping(target = "id", source = "message.id")
    @Mapping(target = "status", source = "message.status")
    @Mapping(target = "answeredMessage", source = "answeredMessage")
    @Mapping(target = "messageText", source = "message.messageText")
    @Mapping(target = "time", source = "message.time")
    @Mapping(target = "isRead", source = "message.isRead")
    @Mapping(target = "isDeleted", source = "message.isDeleted")
    @Mapping(target = "isDelivered", source = "message.isDelivered")
    @Mapping(target = "imageId", source = "message.imageId")
    MessageRs toDTO(Message message, boolean income, Message answeredMessage);
}
