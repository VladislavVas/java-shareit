package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;
    @Autowired
    private UserServiceImpl userService;
    private ItemRequestDto itemRequestDto;
    private UserDto userDto;
    private ItemDto itemDto;

    @BeforeEach
    void getEntities() {
        userDto = UserDto.builder()
                .name("testName")
                .email("test@test.ru")
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("itemDtoName")
                .description("description")
                .available(true)
                .requestId(1L)
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .items(List.of(itemDto))
                .requesterId(1L)
                .description("description")
                .build();
        userDto = userService.addUser(userDto);
        itemRequestDto = itemRequestService.addRequest(itemRequestDto, userDto.getId());
    }

    @Test
    void addRequest() {
        assertEquals(1L, itemRequestDto.getId());
        assertEquals(1L, itemRequestDto.getRequesterId());
        assertEquals("description", itemRequestDto.getDescription());
    }

    @Test
    void getAllRequestsForRequester() {
        List<ItemRequestDto> requestDtoList = itemRequestService.getAllRequestsForRequester(userDto.getId());
        assertEquals(1, requestDtoList.size());
        assertEquals(1L, requestDtoList.get(0).getRequesterId());
        assertEquals("description", requestDtoList.get(0).getDescription());
    }


    @Test
    void getAllRequests() {
        List<ItemRequestDto> requestDtoList = itemRequestService.getAllRequests(userDto.getId(), 0, 20);
        assertEquals(0, requestDtoList.size());
    }

    @Test
    void getRequest() {
        ItemRequestDto result = itemRequestService.getRequest(1L, 1L);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getRequesterId());
        assertEquals("description", result.getDescription());
    }

    @Test
    void getRequestWithException() {
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> itemRequestService.getRequest(1L, 0L));
        assertThat(e.getMessage()).contains(
                String.format("Пользователя с id= " + 0 + " не существует"));
    }

    @Test
    void testWrongPage() {
        ValidateException e = assertThrows(ValidateException.class,
                () -> itemRequestService.getAllRequests(userDto.getId(), -1, -1));
        assertThat(e.getMessage()).contains(
                String.format("Неверные параметры page или size"));
    }
}