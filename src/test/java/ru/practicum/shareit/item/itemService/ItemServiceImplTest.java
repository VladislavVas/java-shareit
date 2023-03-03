package ru.practicum.shareit.item.itemService;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.bookingService.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private BookingServiceImpl bookingService;

    private ItemDto itemDto1;
    private ItemDto itemDto2;
    private UserDto userDto;
    private UserDto userDto2;
    private CommentDto commentDto;
    private BookingRequestDto bookingDto;

    @BeforeEach
    void getEntities() {
        itemDto1 = ItemDto.builder()
                .id(1L)
                .name("itemDto_1_name")
                .description("itemDto_1_description")
                .available(true)
                .requestId(null)
                .build();
        itemDto2 = ItemDto.builder()
                .name("itemDto_2_name")
                .description("itemDto_2_description")
                .available(true)
                .requestId(null)
                .build();
        userDto = UserDto.builder()
                .email("test@test.ru")
                .name("testName")
                .build();
        userDto2 = UserDto.builder()
                .email("booker@test.ru")
                .name("bookerName")
                .build();
        commentDto = CommentDto.builder()
                .text("text")
                .authorName("itemDto_2_name")
                .dateCreate(LocalDateTime.now())
                .build();
        bookingDto = BookingRequestDto.builder()
                .itemId(1L)
                .itemId(1L)
                .start(LocalDateTime.of(2020, 01, 01, 01, 01))
                .end(LocalDateTime.of(2020, 02, 01, 01, 01))
                .build();
        userService.addUser(userDto);
        userService.addUser(userDto2);
    }


    @Test
    void getAllItems() {
        itemService.addNewItem(1, itemDto1);
        itemService.addNewItem(1, itemDto2);
        assertEquals(2, itemService.getAllItems(1, 0, 20).size());
    }


    @Test
    void getItem() {
        itemService.addNewItem(1, itemDto1);
        ItemDtoForRequest itemDto = itemService.getItem(1, 1);
        assertEquals(1, itemDto.getId());
        assertEquals("itemDto_1_name", itemDto.getName());
    }

    @Test
    void getItemWrongId() {
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> itemService.getItem(1, 5));
        assertThat(e.getMessage()).contains(
                String.format("Вещь с id= " + 5 + " не существует"));
    }


    @Test
    void addNewItem() {
        itemService.addNewItem(1, itemDto1);
        ItemDtoForRequest itemDto = itemService.getItem(1, 1);
        assertEquals(1, itemService.getAllItems(1, 0, 20).size());
        assertEquals(1, itemDto.getId());
    }

    @Test
    void updateItem() {
        itemService.addNewItem(1, itemDto1);
        itemService.updateItem(1, 1, itemDto2);
        assertEquals("itemDto_2_name", itemService.getItem(1, 1).getName());
    }

    @Test
    void updateItemWithExeption() {
        itemService.addNewItem(1, itemDto1);
        AccessException e = assertThrows(AccessException.class,
                () -> itemService.updateItem(2, 1, itemDto2));
        assertThat(e.getMessage()).contains(
                String.format("Только собственник может обновлять вещь"));
    }

    @Test
    void deleteItem() {
        itemService.addNewItem(1, itemDto1);
        assertEquals(1, itemService.getAllItems(1, 0, 20).size());
        itemService.deleteItem(1, 1);
        assertEquals(0, itemService.getAllItems(1, 0, 20).size());
    }

    @Test
    void searchItemByText() {
        itemService.addNewItem(1, itemDto1);
        List<ItemDto> itemDto = itemService.searchItemByText("itemDto_1_name", 0, 20);
        assertEquals(1, itemDto.get(0).getId());
    }

    @Test
    void addCommentToItemWithException() {
        itemService.addNewItem(1L, itemDto1);
        ValidateException e = assertThrows(ValidateException.class,
                () -> itemService.addCommentToItem(commentDto, 1L, 1L));
        assertThat(e.getMessage()).contains(
                String.format("Невозможно оставить комментарий"));
    }

    @Test
    void addCommentToItem() {
        itemService.addNewItem(1L, itemDto2);
        bookingService.addBooking(bookingDto, 2L);
        CommentDto result = itemService.addCommentToItem(commentDto, 1L, 2L);
        assertEquals(1, result.getId());
        assertEquals("text", result.getText());
    }
}