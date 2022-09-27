package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.userDto.UserDto;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorageInMemory userStorage;

    @Autowired
    public UserService(UserStorageInMemory userStorage) {
        this.userStorage = userStorage;
    }

    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public UserDto getUserById(long userId) {
        checkId(userId);
        return userStorage.getUserById(userId);
    }

    public UserDto addUser(UserDto userDto) {
        return userStorage.addUser(userDto);
    }

    public UserDto updateUser(long userId, UserDto userDto) {
        checkId(userId);
        UserDto userExist = getUserById(userId);
        return userStorage.updateUser(userId, userExist, userDto);
    }

    public void delete(long userId) {
        checkId(userId);
        userStorage.deleteUser(userId);
    }

    private void checkId(long id) {
        if (userStorage.getUserById(id)==null) {
            throw new NotFoundException("Пользователя с id= " + id + " не существует");
        }
    }
}
