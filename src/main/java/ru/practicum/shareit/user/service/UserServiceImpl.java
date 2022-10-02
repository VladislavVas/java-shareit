package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorageInMemory;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorageInMemory userStorage;
    private final UserMapper mapper;

    public List<UserDto> getAllUsers() {
        return mapper.toDtoList(userStorage.getAllUsers());
    }

    public UserDto getUser(long userId) {
        checkId(userId);
        return mapper.toDto(userStorage.getUserById(userId));
    }

    public UserDto addUser(UserDto userDto) {
        User user = mapper.toUser(userDto);
        return mapper.toDto(userStorage.addUser(user));
    }

    public UserDto updateUser(long userId, UserDto userDto) {
        checkId(userId);
        User userUpdate = mapper.toUser(userDto);
        return mapper.toDto(userStorage.updateUser(userId, userUpdate));
    }

    public void deleteUser(long userId) {
        checkId(userId);
        userStorage.deleteUser(userId);
    }

    private void checkId(long id) {
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException("Пользователя с id= " + id + " не существует");
        }
    }
}
