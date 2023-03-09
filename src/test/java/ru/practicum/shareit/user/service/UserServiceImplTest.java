package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
    private UserDto userDto;
    private UserDto duplicateUserDto;
    private UserDto updateUserDto;


    @BeforeEach
    void getEntities() {
        userDto = UserDto.builder()
                .email("test@test.ru")
                .name("testName")
                .build();

        duplicateUserDto = UserDto.builder()
                .email("test@test.ru")
                .name("testName")
                .build();

        updateUserDto = UserDto.builder()
                .email("update@test.ru")
                .name("updateName")
                .build();
    }

    @Test
    void getAllUsers() {
        userService.addUser(userDto);
        userService.addUser(updateUserDto);
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void getUser() {
        userService.addUser(userDto);
        assertEquals(1, userService.getUser(1).getId());
        assertEquals("testName", userService.getUser(1).getName());
        assertEquals("test@test.ru", userService.getUser(1).getEmail());
    }

    @Test
    void getUserWrongId() {
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> userService.getUser(1L));
        assertThat(e.getMessage()).contains(
                String.format("Пользователя с id= " + 1L + " не существует"));
    }

    @Test
    void addUser() {
        userService.addUser(userDto);
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void addUserWithWrongEmail() {
        userService.addUser(userDto);
        RuntimeException e = assertThrows(RuntimeException.class,
                () -> userService.addUser(duplicateUserDto));
    }

    @Test
    void updateUser() {
        userService.addUser(userDto);
        userService.updateUser(1, updateUserDto);
        assertEquals("updateName", userService.getUser(1).getName());
        assertEquals("update@test.ru", userService.getUser(1).getEmail());
    }

    @Test
    void deleteUser() {
        userService.addUser(userDto);
        userService.deleteUser(1);
        assertEquals(0, userService.getAllUsers().size());
    }
}