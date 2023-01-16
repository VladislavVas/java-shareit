package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class UserStorageInMemory {
    private long userId;
    private final List<User> users = new ArrayList<>();

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(long id) {
        log.info("UserStorage: Запрос пользователя id " + id);
        return users.stream()
                .filter(p -> p.getId() == (id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь id " + id + "не найден")));
    }

    public User addUser(User user) {
        checkExistUser(user);
        user.setId(setUserId());
        users.add(user);
        log.info("UserStorage: Создан пользователь id " + user.getId());
        return user;
    }

    public User updateUser(long id, User userUpdate) {
        User userExist = getUserById(id);
        if (!(userUpdate.getEmail() == null)) {
            checkExistUser(userUpdate);
            userExist.setEmail(userUpdate.getEmail());
            log.info("UserStorage: Пользователю id " + id + " установлен email " + userExist.getEmail());
        }
        if (!(userUpdate.getName() == null)) {
            userExist.setName(userUpdate.getName());
            log.info("UserStorage: Пользователю id " + id + " установлено имя " + userExist.getName());
        }
        return userExist;
    }


    public void deleteUser(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                users.remove(user);
                log.info("UserStorage: удаление пользователя id " + id);
                return;
            }
        }
    }

    private void checkExistUser(User user) {
        for (User userExist : users) {
            if (user.getEmail().equals(userExist.getEmail())) {
                throw new ConflictException("email " + user.getEmail() + " уже используется");
            }
        }
    }

    private long setUserId() {
        return ++userId;
    }
}
