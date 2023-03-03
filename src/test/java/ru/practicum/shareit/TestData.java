package ru.practicum.shareit;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class TestData {

    protected static UserDto userDto;
    protected static UserDto userDto2;
    protected static UserDto duplicateUserDto;
    protected static UserDto updateUserDto;
    protected static User user_1;
    protected static User user_2;
    protected static User user_3;
    protected static Item item;
    protected static ItemDto itemDto_1;
    protected static ItemDto itemDto_2;
    protected static CommentDto commentDto;
    protected static ItemDtoForRequest itemDtoForRequest;
    protected static BookingRequestDto bookingRequestDto;
    protected static BookingResponseDto bookingResponseDto;

    protected static void generateDataForUsers() {
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

        user_1 = User.builder()
                .id(0)
                .name("testName")
                .email("test@test.ru")
                .build();

        user_2 = User.builder()
                .id(2L)
                .name("testName_2")
                .email("test_2@test.ru")
                .build();
    }

    protected static void generateDataForItems() {
        itemDto_1 = ItemDto.builder()
                .id(1L)
                .name("itemDto_1_name")
                .description("itemDto_1_description")
                .available(true)
                .requestId(null)
                .build();

        itemDto_2 = ItemDto.builder()
                .name("itemDto_2_name")
                .description("itemDto_2_description")
                .available(true)
                .requestId(null)
                .build();

        commentDto = CommentDto.builder()
                .authorName("itemDto_1_name")
                .text("commentText")
                .build();

        itemDtoForRequest = ItemDtoForRequest.builder()
                .id(1L)
                .available(true)
                .name("itemDto")
                .description("for request")
                .build();

        item = Item.builder()
                .id(1L)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .owner(user_2)
                .build();
    }

    protected static void generateDataForBooking() {
        bookingRequestDto = BookingRequestDto.builder()
                .start(LocalDateTime.of(2025, 01, 01, 01, 01))
                .end(LocalDateTime.of(2025, 02, 01, 01, 01))
                .itemId(1L)
                .build();

        bookingResponseDto = BookingResponseDto.builder()
                .id(1l)
                .start(LocalDateTime.of(2025, 01, 01, 01, 01))
                .end(LocalDateTime.of(2025, 02, 01, 01, 01))
                .booker(null)
                .item(null)
                .status(Status.WAITING)
                .build();

        userDto2 = UserDto.builder()
                .email("booker@test.ru")
                .name("bookerName")
                .build();

        user_1 = User.builder()
                .name("user_1")
                .email("user_1@test.ru")
                .build();
        user_2 = User.builder()
                .name("user_2")
                .email("user_2@test.ru")
                .build();
        user_3 = User.builder()
                .name("user_3")
                .email("user_#@test.ru")
                .build();
        item = Item.builder()
                .name("item")
                .description("description")
                .available(true)
                .owner(user_1)
                .itemRequest(null)
                .build();




//                new Item("item", "item description", true, owner, null);
//        item = ItemMapper.toItem(itemService.addItem(ItemMapper.toItemDto(item), owner.getId()), owner, null);
//
//        bookItemRequestDto = new BookItemRequestDto(start, end, item.getId());
//        bookingDto = bookingService.addBooking(bookItemRequestDto, booker.getId());
    }
}
