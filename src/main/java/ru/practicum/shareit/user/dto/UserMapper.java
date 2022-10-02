package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public static User toUser(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<User> toListUser(List<UserDto> dtoList) {
        return dtoList.stream()
                .map(UserMapper::toUser)
                .collect(Collectors.toList());
    }

    public static List<UserDto> toDtoList(List<User> userList) {
        return userList.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}
