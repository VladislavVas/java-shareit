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
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userMapper.toDtoList(userStorage.findAll());
    }

    public UserDto getUser(long userId) {
        User user = checkId(userId);
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return userMapper.toDto(userStorage.save(user));
    }

    @Transactional
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = checkId(userId);
        User userUpdate = userMapper.toUser(userDto);
        if (userUpdate.getName() != null) {
            user.setName(userUpdate.getName());
        }
        if (userUpdate.getEmail() != null) {
            user.setEmail(userUpdate.getEmail());
        }
        return userMapper.toDto(userStorage.save(user));
    }

    @Transactional
    public void deleteUser(long userId) {
        checkId(userId);
        userStorage.deleteById(userId);
    }

    private User checkId(long id) {
        Optional<User> user = userStorage.findById(id);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователя с id= " + id + " не существует");
        } else {
            return user.get();
        }
    }
}
