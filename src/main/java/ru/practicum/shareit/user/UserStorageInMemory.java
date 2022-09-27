package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.userDto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class UserStorageInMemory {
    private long userId;
    private final List<UserDto> users = new ArrayList<>();

    public List<UserDto> getAllUsers() {
        return users;
    }

    public UserDto getUserById(long id) {
        log.info("UserStorage: Запрос пользователя id " + id);
        return users.stream()
                .filter(p -> p.getId() == (id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь id " + id + "не найден")));
    }

    public UserDto addUser(UserDto userDto) {
        checkExistUser(userDto);
        userDto.setId(setUserId());
        users.add(userDto);
        log.info("UserStorage: Создан пользователь id " + userDto.getId());
        return userDto;
    }

    public UserDto updateUser(long id, UserDto userUpdate, UserDto userDto) {
        if (!(userDto.getEmail() == null)) {
            checkExistUser(userDto);
            userUpdate.setEmail(userDto.getEmail());
            log.info("UserStorage: Пользователю id " + id + " установлен email " + userDto.getEmail());
        }
        if (!(userDto.getName() == null)) {
            userUpdate.setName(userDto.getName());
            log.info("UserStorage: Пользователю id " + id + " установлено имя " + userDto.getName());
        }
        return userUpdate;
    }

    public void deleteUser(long id) {
        for (UserDto user : users) {
            if (user.getId() == id) {
                users.remove(user);
                log.info("UserStorage: удаление пользователя id " + id);
                return;
            }
        }
    }

    public void checkExistUser(UserDto userDto) {
        for (UserDto userExist : users) {
            if (userDto.getEmail().equals(userExist.getEmail())) {
                throw new ConflictException("email " + userDto.getEmail() + " уже используется");
            }
        }
    }

    private long setUserId() {
        return ++userId;
    }
}
