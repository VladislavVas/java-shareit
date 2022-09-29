package ru.practicum.shareit.user.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    User toUser(UserDto dto);

    UserDto toDto(User user);

    List<User> toListUser (List<UserDto> dtoList);

    List<UserDto> toDtoList (List<User> userList);
}
