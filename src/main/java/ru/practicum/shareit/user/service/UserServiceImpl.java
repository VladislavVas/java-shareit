package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public List<UserDto> getAllUsers() {
        return UserMapper.toDtoList(userStorage.findAll());
    }

    public UserDto getUser(long userId) {
        User user = getUserFromStorage(userId);
        return UserMapper.toDto(user);

    }

    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toDto(userStorage.save(user));
    }

    @Transactional
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = getUserFromStorage(userId);
        User userUpdate = UserMapper.toUser(userDto);
        if (userUpdate.getName() != null) {
            user.setName(userUpdate.getName());
        }
        if (userUpdate.getEmail() != null) {
            user.setEmail(userUpdate.getEmail());
        }
        return UserMapper.toDto(userStorage.save(user));
    }

    @Transactional
    public void deleteUser(long userId) {
        if (userStorage.existsById(userId)) {
            userStorage.deleteById(userId);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private User getUserFromStorage(long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с id= " + id + " не существует"));
    }
}
