package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private UserDto userDto;
    private UserDto updateUserDto;

    @BeforeEach
    void getEntities() {
        userDto = UserDto.builder()
                .email("test@test.ru")
                .name("testName")
                .build();
        updateUserDto = UserDto.builder()
                .email("update@test.ru")
                .name("updateName")
                .build();
    }

    @Test
    void addUser() throws Exception {
        when(userService.addUser(any())).thenReturn(userDto);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.email").value("test@test.ru"));
        verify(userService, times(1))
                .addUser(userDto);
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUser(0)).thenReturn(userDto);
        mvc.perform(get("/users/{userId}", userDto.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
        verify(userService, times(1))
                .getUser(0);
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(anyLong(), any())).thenReturn(updateUserDto);
        mvc.perform(patch("/users/{userId}", 0)
                        .content(mapper.writeValueAsString(updateUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.name").value(updateUserDto.getName()))
                .andExpect(jsonPath("$.email").value(updateUserDto.getEmail()));
        verify(userService, times(1))
                .updateUser(0, updateUserDto);
    }

    @Test
    void getAllUsers() throws Exception {
        List<UserDto> users = new ArrayList<>(
                Arrays.asList(userDto, updateUserDto));
        when(userService.getAllUsers()).thenReturn(users);
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(users.size()));
        verify(userService, times(1))
                .getAllUsers();
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(0);
        mvc.perform(delete("/users/{userId}", 0))
                .andExpect(status().isOk());
        verify(userService, times(1))
                .deleteUser(0);
    }
}
