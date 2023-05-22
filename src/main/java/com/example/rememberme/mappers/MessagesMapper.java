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
    MessageRs toDTO(Message message, boolean income);
}
