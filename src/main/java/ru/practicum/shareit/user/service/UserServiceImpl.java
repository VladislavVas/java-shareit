package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserStorageInMemory;
import ru.practicum.shareit.user.userDto.UserDto;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorageInMemory userStorage;

    @Autowired
    public UserServiceImpl(UserStorageInMemory userStorage) {
        this.userStorage = userStorage;
    }

    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public UserDto getUser(long userId) {
        checkId(userId);
        return userStorage.getUserById(userId);
    }

    public UserDto addUser(UserDto userDto) {
        return userStorage.addUser(userDto);
    }

    public UserDto updateUser(long userId, UserDto userDto) {
        checkId(userId);
        UserDto userExist = getUser(userId);
        return userStorage.updateUser(userId, userExist, userDto);
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
