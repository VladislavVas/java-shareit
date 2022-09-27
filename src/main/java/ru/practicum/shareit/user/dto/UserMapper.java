package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.model.User;

@Mapper
public interface UserMapper {
    User toUser(UserDto dto);

    UserDto toDto(User user);
}
