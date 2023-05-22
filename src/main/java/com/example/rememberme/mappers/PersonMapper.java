package com.example.rememberme.mappers;

import com.example.rememberme.api.PersonRs;
import com.example.rememberme.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonRs toDTO(Person person);
}
